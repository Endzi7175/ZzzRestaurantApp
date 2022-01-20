package com.ktsnwt.restaurant.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.ktsnwt.restaurant.domain.Picture;
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
 * Spring Data Elasticsearch repository for the {@link Picture} entity.
 */
public interface PictureSearchRepository extends ElasticsearchRepository<Picture, Long>, PictureSearchRepositoryInternal {}

interface PictureSearchRepositoryInternal {
    Page<Picture> search(String query, Pageable pageable);
}

class PictureSearchRepositoryInternalImpl implements PictureSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    PictureSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Picture> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Picture> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Picture.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
