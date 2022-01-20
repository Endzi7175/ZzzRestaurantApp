package com.ktsnwt.restaurant.service;

import com.ktsnwt.restaurant.service.dto.RestaurantOrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ktsnwt.restaurant.domain.RestaurantOrder}.
 */
public interface RestaurantOrderService {
    /**
     * Save a restaurantOrder.
     *
     * @param restaurantOrderDTO the entity to save.
     * @return the persisted entity.
     */
    RestaurantOrderDTO save(RestaurantOrderDTO restaurantOrderDTO);

    /**
     * Partially updates a restaurantOrder.
     *
     * @param restaurantOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RestaurantOrderDTO> partialUpdate(RestaurantOrderDTO restaurantOrderDTO);

    /**
     * Get all the restaurantOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" restaurantOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestaurantOrderDTO> findOne(Long id);

    /**
     * Delete the "id" restaurantOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the restaurantOrder corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantOrderDTO> search(String query, Pageable pageable);
}
