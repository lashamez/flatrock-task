package com.flatrock.product.service;

import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.model.ProductAvailabilityRequest;
import com.flatrock.common.model.ProductAvailabilityResponse;
import com.flatrock.common.model.SellerItemData;
import com.flatrock.product.repository.StockRepository;
import com.flatrock.product.domain.Product;
import com.flatrock.product.domain.StockProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Save a stock.
     *
     * @param stock the entity to save.
     * @return the persisted entity.
     */
    public StockProduct save(StockProduct stock) {
        log.debug("Request to save Stock : {}", stock);
        return stockRepository.save(stock);
    }

    /**
     * Partially update a stock.
     *
     * @param stock the entity to update partially.
     * @return the persisted entity.
     */
    @CachePut(value = "productById", key = "#stock.id")
    public Optional<StockProduct> partialUpdate(StockProduct stock) {
        log.debug("Request to partially update Stock : {}", stock);

        return stockRepository
            .findById(stock.getId())
            .map(existingStock -> {
                if (stock.getQuantity() != null) {
                    existingStock.setQuantity(stock.getQuantity());
                }

                return existingStock;
            })
            .map(stockRepository::save);
    }

    /**
     * Get all the stocks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StockProduct> findAll() {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll();
    }
    /**
     * Get one stock by productId.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    @Cacheable("productById")
    public Optional<StockProduct> findOneByProductId(Long id) {
        log.debug("Request to get Stock by Product: {}", id);
        return stockRepository.findOneByProductId(id);
    }

    @Transactional(readOnly = true)
    public List<ProductAvailabilityResponse> getAvailabilityResponse(List<ProductAvailabilityRequest> requests) {
        List<Long> productIds = requests.stream().map(ProductAvailabilityRequest::getProductId).toList();
        List<StockProduct> stockProducts = stockRepository.findByProductIds(productIds);
        List<ProductAvailabilityResponse> responses = new ArrayList<>();
        if (requests.size() != stockProducts.size()) {
            throw new BadRequestAlertException("Incorrect productIds", "product", "invalidproduct");
        }
        requests.forEach(entry->{
            //since requests.size() = stockProducts.size() Optional.get() is safe.
            StockProduct stockProduct = stockProducts.stream()
                .filter(stock -> stock.getProduct().getId() == entry.getProductId())
                .findFirst().get();
            if (stockProduct.getQuantity() >= entry.getQuantity()) {
                Product product = stockProduct.getProduct();
                responses.add(new ProductAvailabilityResponse(entry.getProductId(),true, product.getPrice() * entry.getQuantity()));
            } else {
                responses.add(new ProductAvailabilityResponse(entry.getProductId(),false, null));
            }
        });
        return responses;
    }

    @Transactional
    public void reversalQuantity(OrderItemDto item) {
        stockRepository.increaseProductQuantity(item.getProductId(), item.getQuantity());
    }

    @Transactional
    public void reduceQuantities(List<OrderItemDto> items) {
        items.forEach(item -> stockRepository.reduceProductQuantity(item.getProductId(), item.getQuantity()));
    }


    public OrderSellersData getSellersData(List<OrderItemDto> orderItems) {
        List<StockProduct> stockProducts = stockRepository.findByProductIds(orderItems.stream().map(OrderItemDto::getProductId).toList());
        OrderSellersData sellerItemData = new OrderSellersData();
        Map<Long, List<StockProduct>> sellerProducts = new HashMap<>();
        stockProducts.forEach(stock -> sellerProducts.computeIfAbsent(stock.getProduct().getSellerId(),
            id -> new ArrayList<>()).add(stock));
        sellerProducts.forEach((key, value) -> {
            List<Long> clientProducts = value.stream().map(stock -> stock.getProduct().getId()).toList();
            sellerItemData.addSellerItemData(new SellerItemData(key, orderItems.stream()
                .filter(orderItem -> clientProducts.contains(orderItem.getProductId())).toList()));
        });
        return sellerItemData;
    }
}
