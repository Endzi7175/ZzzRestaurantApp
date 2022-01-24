package com.ktsnwt.restaurant.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ktsnwt.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestaurantOrderItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestaurantOrderItemDTO.class);
        RestaurantOrderItemDTO restaurantOrderItemDTO1 = new RestaurantOrderItemDTO();
        restaurantOrderItemDTO1.setId(1L);
        RestaurantOrderItemDTO restaurantOrderItemDTO2 = new RestaurantOrderItemDTO();
        assertThat(restaurantOrderItemDTO1).isNotEqualTo(restaurantOrderItemDTO2);
        restaurantOrderItemDTO2.setId(restaurantOrderItemDTO1.getId());
        assertThat(restaurantOrderItemDTO1).isEqualTo(restaurantOrderItemDTO2);
        restaurantOrderItemDTO2.setId(2L);
        assertThat(restaurantOrderItemDTO1).isNotEqualTo(restaurantOrderItemDTO2);
        restaurantOrderItemDTO1.setId(null);
        assertThat(restaurantOrderItemDTO1).isNotEqualTo(restaurantOrderItemDTO2);
    }
}
