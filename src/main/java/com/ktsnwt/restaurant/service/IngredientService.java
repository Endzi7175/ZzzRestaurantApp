package com.ktsnwt.restaurant.service;

import com.ktsnwt.restaurant.service.dto.IngredientDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ktsnwt.restaurant.domain.Ingredient}.
 */
public interface IngredientService {
    /**
     * Save a ingredient.
     *
     * @param ingredientDTO the entity to save.
     * @return the persisted entity.
     */
    IngredientDTO save(IngredientDTO ingredientDTO);

    /**
     * Partially updates a ingredient.
     *
     * @param ingredientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IngredientDTO> partialUpdate(IngredientDTO ingredientDTO);

    /**
     * Get all the ingredients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IngredientDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ingredient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IngredientDTO> findOne(Long id);

    /**
     * Delete the "id" ingredient.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ingredient corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IngredientDTO> search(String query, Pageable pageable);
}
