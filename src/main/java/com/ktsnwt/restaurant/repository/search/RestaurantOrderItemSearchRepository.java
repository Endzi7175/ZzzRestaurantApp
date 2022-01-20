package com.ktsnwt.restaurant.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.ktsnwt.restaurant.domain.RestaurantOrderItem;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RestaurantOrderItem} entity.
 */
public interface RestaurantOrderItemSearchRepository
    extends ElasticsearchRepository<RestaurantOrderItem, Long>, RestaurantOrderItemSearchRepositoryInternal {}

interface RestaurantOrderItemSearchRepositoryInternal {
    Page<RestaurantOrderItem> search(String query, Pageable pageable);
}

class RestaurantOrderItemSearchRepositoryInternalImpl implements RestaurantOrderItemSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    RestaurantOrderItemSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<RestaurantOrderItem> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<RestaurantOrderItem> hits = elasticsearchTemplate
            .search(nativeSearchQuery, RestaurantOrderItem.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
