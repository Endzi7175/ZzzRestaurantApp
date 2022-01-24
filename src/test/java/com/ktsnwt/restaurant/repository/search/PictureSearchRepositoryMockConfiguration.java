package com.ktsnwt.restaurant.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PictureSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PictureSearchRepositoryMockConfiguration {

    @MockBean
    private PictureSearchRepository mockPictureSearchRepository;
}
