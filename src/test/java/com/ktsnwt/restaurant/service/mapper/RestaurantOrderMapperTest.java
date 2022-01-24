package com.ktsnwt.restaurant.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantOrderMapperTest {

    private RestaurantOrderMapper restaurantOrderMapper;

    @BeforeEach
    public void setUp() {
        restaurantOrderMapper = new RestaurantOrderMapperImpl();
    }
}
