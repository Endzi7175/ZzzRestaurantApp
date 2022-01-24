package com.ktsnwt.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ktsnwt.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestaurantOrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestaurantOrderItem.class);
        RestaurantOrderItem restaurantOrderItem1 = new RestaurantOrderItem();
        restaurantOrderItem1.setId(1L);
        RestaurantOrderItem restaurantOrderItem2 = new RestaurantOrderItem();
        restaurantOrderItem2.setId(restaurantOrderItem1.getId());
        assertThat(restaurantOrderItem1).isEqualTo(restaurantOrderItem2);
        restaurantOrderItem2.setId(2L);
        assertThat(restaurantOrderItem1).isNotEqualTo(restaurantOrderItem2);
        restaurantOrderItem1.setId(null);
        assertThat(restaurantOrderItem1).isNotEqualTo(restaurantOrderItem2);
    }
}
