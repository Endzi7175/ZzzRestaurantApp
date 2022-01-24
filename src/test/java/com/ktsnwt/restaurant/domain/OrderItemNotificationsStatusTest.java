package com.ktsnwt.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ktsnwt.restaurant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemNotificationsStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemNotificationsStatus.class);
        OrderItemNotificationsStatus orderItemNotificationsStatus1 = new OrderItemNotificationsStatus();
        orderItemNotificationsStatus1.setId(1L);
        OrderItemNotificationsStatus orderItemNotificationsStatus2 = new OrderItemNotificationsStatus();
        orderItemNotificationsStatus2.setId(orderItemNotificationsStatus1.getId());
        assertThat(orderItemNotificationsStatus1).isEqualTo(orderItemNotificationsStatus2);
        orderItemNotificationsStatus2.setId(2L);
        assertThat(orderItemNotificationsStatus1).isNotEqualTo(orderItemNotificationsStatus2);
        orderItemNotificationsStatus1.setId(null);
        assertThat(orderItemNotificationsStatus1).isNotEqualTo(orderItemNotificationsStatus2);
    }
}
