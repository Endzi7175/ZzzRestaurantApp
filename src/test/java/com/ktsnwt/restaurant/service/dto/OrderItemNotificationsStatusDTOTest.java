package com.ktsnwt.restaurant.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ktsnwt.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemNotificationsStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemNotificationsStatusDTO.class);
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO1 = new OrderItemNotificationsStatusDTO();
        orderItemNotificationsStatusDTO1.setId(1L);
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO2 = new OrderItemNotificationsStatusDTO();
        assertThat(orderItemNotificationsStatusDTO1).isNotEqualTo(orderItemNotificationsStatusDTO2);
        orderItemNotificationsStatusDTO2.setId(orderItemNotificationsStatusDTO1.getId());
        assertThat(orderItemNotificationsStatusDTO1).isEqualTo(orderItemNotificationsStatusDTO2);
        orderItemNotificationsStatusDTO2.setId(2L);
        assertThat(orderItemNotificationsStatusDTO1).isNotEqualTo(orderItemNotificationsStatusDTO2);
        orderItemNotificationsStatusDTO1.setId(null);
        assertThat(orderItemNotificationsStatusDTO1).isNotEqualTo(orderItemNotificationsStatusDTO2);
    }
}
