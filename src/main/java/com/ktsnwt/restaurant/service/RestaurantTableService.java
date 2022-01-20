package com.ktsnwt.restaurant.service;

import com.ktsnwt.restaurant.service.dto.RestaurantTableDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ktsnwt.restaurant.domain.RestaurantTable}.
 */
public interface RestaurantTableService {
    /**
     * Save a restaurantTable.
     *
     * @param restaurantTableDTO the entity to save.
     * @return the persisted entity.
     */
    RestaurantTableDTO save(RestaurantTableDTO restaurantTableDTO);

    /**
     * Partially updates a restaurantTable.
     *
     * @param restaurantTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RestaurantTableDTO> partialUpdate(RestaurantTableDTO restaurantTableDTO);

    /**
     * Get all the restaurantTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantTableDTO> findAll(Pageable pageable);

    /**
     * Get the "id" restaurantTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestaurantTableDTO> findOne(Long id);

    /**
     * Delete the "id" restaurantTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the restaurantTable corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurantTableDTO> search(String query, Pageable pageable);
}
