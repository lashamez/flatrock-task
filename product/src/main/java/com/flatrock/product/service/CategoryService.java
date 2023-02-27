package com.flatrock.product.service;

import com.flatrock.product.repository.CategoryRepository;
import com.flatrock.product.domain.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    @Cacheable("categories")
    public List<Category> findAll() {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll();
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "category")
    public Optional<Category> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id);
    }

}
