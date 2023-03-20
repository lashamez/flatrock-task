package com.flatrock.product.web;

import com.flatrock.product.domain.ESCategory;
import com.flatrock.product.domain.ESProduct;
import com.flatrock.product.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {
    private final Logger log = LoggerFactory.getLogger(StockResource.class);
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<ESProduct>> getAllStocks(@RequestParam("query") String query) throws IOException {
        log.debug("REST request to get all Stocks");
        return ResponseEntity.ok(searchService.searchAutocomplete(query));
    }

    @GetMapping("/category")
    public ResponseEntity<List<ESCategory>> getCategories() throws IOException {
        return ResponseEntity.ok(searchService.getCategories());
    }

}
