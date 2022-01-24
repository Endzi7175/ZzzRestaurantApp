package com.ktsnwt.restaurant.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link OrderItemNotificationsStatusSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OrderItemNotificationsStatusSearchRepositoryMockConfiguration {

    @MockBean
    private OrderItemNotificationsStatusSearchRepository mockOrderItemNotificationsStatusSearchRepository;
}
