package com.flatrock.product.repository;

import com.flatrock.product.domain.ESProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@Repository
public interface ProductESRepository extends ElasticsearchRepository<ESProduct, Long> {
    @Query("{\"bool\": {\"filter\": [{\"term\": {\"categoryName\": \"?0\"}}]}}")
    Page<ESProduct> findByCategoryName(String categoryName, Pageable pageable);

    default Page<ESProduct> findByCategoryNameEncoded(String encodedCategoryName, Pageable pageable) {
        String categoryName = URLDecoder.decode(encodedCategoryName, StandardCharsets.UTF_8);
        return findByCategoryName(categoryName, pageable);
    }
}