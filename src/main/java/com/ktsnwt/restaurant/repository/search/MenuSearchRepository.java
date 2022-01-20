package com.ktsnwt.restaurant.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.ktsnwt.restaurant.domain.Menu;
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
 * Spring Data Elasticsearch repository for the {@link Menu} entity.
 */
public interface MenuSearchRepository extends ElasticsearchRepository<Menu, Long>, MenuSearchRepositoryInternal {}

interface MenuSearchRepositoryInternal {
    Page<Menu> search(String query, Pageable pageable);
}

class MenuSearchRepositoryInternalImpl implements MenuSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    MenuSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Menu> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Menu> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Menu.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
