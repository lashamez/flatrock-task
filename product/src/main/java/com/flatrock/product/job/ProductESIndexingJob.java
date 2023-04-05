package com.flatrock.product.job;

import com.flatrock.product.domain.ESProduct;
import com.flatrock.product.mapper.ProductMapper;
import com.flatrock.product.repository.ProductESRepository;
import com.flatrock.product.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ProductESIndexingJob {
    private final Logger log = LoggerFactory.getLogger(ProductESIndexingJob.class);

    private final StockRepository stockRepository;
    private final ProductESRepository productRepository;

    public ProductESIndexingJob(StockRepository stockRepository, ProductESRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }


    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.MINUTES)
    public void indexProducts() {
        long start = System.currentTimeMillis();
        log.debug("Starting product indexing job");
        List<ESProduct> products = stockRepository.findAll()
                .stream().map(ProductMapper::toEsProduct).toList();
        productRepository.saveAll(products);
        long finish = System.currentTimeMillis();
        log.debug("Product indexing job finished successfully in {}s", (finish - start) / 1000);
    }
}
