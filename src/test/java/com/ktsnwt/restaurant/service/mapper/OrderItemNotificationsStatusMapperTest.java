package com.ktsnwt.restaurant.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderItemNotificationsStatusMapperTest {

    private OrderItemNotificationsStatusMapper orderItemNotificationsStatusMapper;

    @BeforeEach
    public void setUp() {
        orderItemNotificationsStatusMapper = new OrderItemNotificationsStatusMapperImpl();
    }
}
