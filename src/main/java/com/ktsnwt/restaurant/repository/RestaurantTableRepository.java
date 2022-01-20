package com.ktsnwt.restaurant.repository;

import com.ktsnwt.restaurant.domain.RestaurantTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RestaurantTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {}
