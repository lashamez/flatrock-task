package com.flatrock.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Document(indexName = "products")
@Mapping(mappingPath = "es-product-mappings.json")
@Setting(settingPath = "es-product-settings.json")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ESProduct implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String name;

    private BigDecimal price;

    private Long sellerId;

    private String categoryName;

    private Integer quantity;

    private String shortDescription;

    private String fullDescription;

    public void setPrice(double price) {
        this.price = BigDecimal.valueOf(price);
    }
}