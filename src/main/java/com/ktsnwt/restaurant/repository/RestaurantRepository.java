package com.ktsnwt.restaurant.repository;

import com.ktsnwt.restaurant.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaurant entity.
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query(
        value = "select distinct restaurant from Restaurant restaurant left join fetch restaurant.users",
        countQuery = "select count(distinct restaurant) from Restaurant restaurant"
    )
    Page<Restaurant> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct restaurant from Restaurant restaurant left join fetch restaurant.users")
    List<Restaurant> findAllWithEagerRelationships();

    @Query("select restaurant from Restaurant restaurant left join fetch restaurant.users where restaurant.id =:id")
    Optional<Restaurant> findOneWithEagerRelationships(@Param("id") Long id);


}
