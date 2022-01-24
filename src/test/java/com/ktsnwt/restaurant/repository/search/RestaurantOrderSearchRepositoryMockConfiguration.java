package com.ktsnwt.restaurant.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RestaurantOrderSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RestaurantOrderSearchRepositoryMockConfiguration {

    @MockBean
    private RestaurantOrderSearchRepository mockRestaurantOrderSearchRepository;
}
