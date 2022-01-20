package com.ktsnwt.restaurant.repository;

import com.ktsnwt.restaurant.domain.MenuItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MenuItem entity.
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query(
        value = "select distinct menuItem from MenuItem menuItem left join fetch menuItem.ingredients",
        countQuery = "select count(distinct menuItem) from MenuItem menuItem"
    )
    Page<MenuItem> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct menuItem from MenuItem menuItem left join fetch menuItem.ingredients")
    List<MenuItem> findAllWithEagerRelationships();

    @Query("select menuItem from MenuItem menuItem left join fetch menuItem.ingredients where menuItem.id =:id")
    Optional<MenuItem> findOneWithEagerRelationships(@Param("id") Long id);
}
