package com.ktsnwt.restaurant.repository;

import com.ktsnwt.restaurant.domain.RestaurantOrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RestaurantOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantOrderItemRepository extends JpaRepository<RestaurantOrderItem, Long> {}
