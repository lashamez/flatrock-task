package com.flatrock.product.repository;

import com.flatrock.product.domain.ESProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductESRepository extends ElasticsearchRepository<ESProduct, Long> {
}