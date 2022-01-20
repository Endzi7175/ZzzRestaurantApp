package com.ktsnwt.restaurant.repository;

import com.ktsnwt.restaurant.domain.OrderItemNotificationsStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderItemNotificationsStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemNotificationsStatusRepository extends JpaRepository<OrderItemNotificationsStatus, Long> {}
