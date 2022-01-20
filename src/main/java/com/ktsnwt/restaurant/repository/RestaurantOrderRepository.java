package com.ktsnwt.restaurant.repository;

import com.ktsnwt.restaurant.domain.RestaurantOrder;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RestaurantOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Long> {
    @Query("select restaurantOrder from RestaurantOrder restaurantOrder where restaurantOrder.user.login = ?#{principal.username}")
    List<RestaurantOrder> findByUserIsCurrentUser();
}
