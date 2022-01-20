package com.ktsnwt.restaurant.service;

import com.ktsnwt.restaurant.service.dto.RestaurantOrderItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ktsnwt.restaurant.domain.RestaurantOrderItem}.
 */
public interface RestaurantOrderItemService {
    /**
     * Save a restaurantOrderItem.
     *
     * @param restaurantOrderItemDTO the entity to save.
     * @return the persisted entity.
     */
    RestaurantOrderItemDTO save(RestaurantOrderItemDTO restaurantOrderItemDTO);

    /**
     * Partially updates a restaurantOrderItem.
     *
     * @param restaurantOrderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RestaurantOrderItemDTO> partialUpdate(RestaurantOrderItemDTO restaurantOrderItemDTO);

    /**
     * Get all the restaurantOrderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantOrderItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" restaurantOrderItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestaurantOrderItemDTO> findOne(Long id);

    /**
     * Delete the "id" restaurantOrderItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the restaurantOrderItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantOrderItemDTO> search(String query, Pageable pageable);
}
