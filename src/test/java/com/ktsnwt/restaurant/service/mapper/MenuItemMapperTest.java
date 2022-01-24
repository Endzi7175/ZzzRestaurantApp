package com.ktsnwt.restaurant.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuItemMapperTest {

    private MenuItemMapper menuItemMapper;

    @BeforeEach
    public void setUp() {
        menuItemMapper = new MenuItemMapperImpl();
    }
}
