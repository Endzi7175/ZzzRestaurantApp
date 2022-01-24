package com.ktsnwt.restaurant.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RestaurantTableSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RestaurantTableSearchRepositoryMockConfiguration {

    @MockBean
    private RestaurantTableSearchRepository mockRestaurantTableSearchRepository;
}
