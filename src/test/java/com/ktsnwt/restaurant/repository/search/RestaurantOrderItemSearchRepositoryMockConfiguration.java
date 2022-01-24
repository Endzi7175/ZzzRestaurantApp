package com.ktsnwt.restaurant.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RestaurantOrderItemSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RestaurantOrderItemSearchRepositoryMockConfiguration {

    @MockBean
    private RestaurantOrderItemSearchRepository mockRestaurantOrderItemSearchRepository;
}
