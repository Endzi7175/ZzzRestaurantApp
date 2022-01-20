package com.ktsnwt.restaurant.service;

import com.ktsnwt.restaurant.service.dto.MenuItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ktsnwt.restaurant.domain.MenuItem}.
 */
public interface MenuItemService {
    /**
     * Save a menuItem.
     *
     * @param menuItemDTO the entity to save.
     * @return the persisted entity.
     */
    MenuItemDTO save(MenuItemDTO menuItemDTO);

    /**
     * Partially updates a menuItem.
     *
     * @param menuItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO);

    /**
     * Get all the menuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MenuItemDTO> findAll(Pageable pageable);

    /**
     * Get all the menuItems with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MenuItemDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" menuItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MenuItemDTO> findOne(Long id);

    /**
     * Delete the "id" menuItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the menuItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MenuItemDTO> search(String query, Pageable pageable);
}
