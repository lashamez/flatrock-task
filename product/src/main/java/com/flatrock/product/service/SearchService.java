package com.flatrock.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.flatrock.product.domain.ESCategory;
import com.flatrock.product.domain.ESProduct;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    public List<ESCategory> getCategories() throws IOException {

        Aggregation aggregation = AggregationBuilders.terms()
                .field("categoryName").build()._toAggregation();
        Query query = QueryBuilders.matchAllQueryAsQuery();
        SearchRequest searchRequest = new SearchRequest.Builder().query(query).size(0)
                .aggregations(Map.of("categories", aggregation)).build();
        return client.search(searchRequest, Object.class).aggregations().get("categories")
                .sterms().buckets().array().stream()
                .map(bucket -> new ESCategory(bucket.key().stringValue(), bucket.docCount()))
                .toList();
    }
}
