package com.flatrock.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.flatrock.product.domain.ESProduct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchService {
    private final ElasticsearchClient client;

    public SearchService(ElasticsearchClient client) {
        this.client = client;
    }

    public List<ESProduct> searchAutocomplete(String queryString) throws IOException {
        MatchQuery autocomplete = new MatchQuery.Builder().field("autocomplete").query(queryString).build();
        Query query = new Query(autocomplete);
        return client.search(new SearchRequest.Builder().query(query).build(), ESProduct.class)
                .hits().hits().stream().map(Hit::source).toList();
    }
}
