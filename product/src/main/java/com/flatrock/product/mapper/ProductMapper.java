package com.flatrock.product.mapper;

import com.flatrock.product.domain.ESProduct;
import com.flatrock.product.domain.StockProduct;

public class ProductMapper {
    public static ESProduct toEsProduct(StockProduct product) {
        return ESProduct.builder()
                .id(product.getId())
                .name(product.getProduct().getName())
                .categoryName(product.getProduct().getCategory().getCategoryName())
                .price(product.getProduct().getPrice())
                .sellerId(product.getProduct().getSellerId())
                .quantity(product.getQuantity())
                .shortDescription(product.getProduct().getShortDescription())
                .fullDescription(product.getProduct().getFullDescription())
                .build();
    }
}
