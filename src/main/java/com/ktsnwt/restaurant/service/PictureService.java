package com.ktsnwt.restaurant.service;

import com.ktsnwt.restaurant.service.dto.PictureDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ktsnwt.restaurant.domain.Picture}.
 */
public interface PictureService {
    /**
     * Save a picture.
     *
     * @param pictureDTO the entity to save.
     * @return the persisted entity.
     */
    PictureDTO save(PictureDTO pictureDTO);

    /**
     * Partially updates a picture.
     *
     * @param pictureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PictureDTO> partialUpdate(PictureDTO pictureDTO);

    /**
     * Get all the pictures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PictureDTO> findAll(Pageable pageable);
    /**
     * Get all the PictureDTO where MenuItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PictureDTO> findAllWhereMenuItemIsNull();

    /**
     * Get the "id" picture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PictureDTO> findOne(Long id);

    /**
     * Delete the "id" picture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the picture corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PictureDTO> search(String query, Pageable pageable);
}
