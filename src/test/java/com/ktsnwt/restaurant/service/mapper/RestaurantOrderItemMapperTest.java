package com.ktsnwt.restaurant.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestaurantOrderItemMapperTest {

    private RestaurantOrderItemMapper restaurantOrderItemMapper;

    @BeforeEach
    public void setUp() {
        restaurantOrderItemMapper = new RestaurantOrderItemMapperImpl();
    }
}
