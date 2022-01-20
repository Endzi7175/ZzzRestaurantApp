package com.ktsnwt.restaurant.service;

import com.ktsnwt.restaurant.service.dto.OrderItemNotificationsStatusDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ktsnwt.restaurant.domain.OrderItemNotificationsStatus}.
 */
public interface OrderItemNotificationsStatusService {
    /**
     * Save a orderItemNotificationsStatus.
     *
     * @param orderItemNotificationsStatusDTO the entity to save.
     * @return the persisted entity.
     */
    OrderItemNotificationsStatusDTO save(OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO);

    /**
     * Partially updates a orderItemNotificationsStatus.
     *
     * @param orderItemNotificationsStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderItemNotificationsStatusDTO> partialUpdate(OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO);

    /**
     * Get all the orderItemNotificationsStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderItemNotificationsStatusDTO> findAll(Pageable pageable);
    /**
     * Get all the OrderItemNotificationsStatusDTO where OrderItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrderItemNotificationsStatusDTO> findAllWhereOrderItemIsNull();

    /**
     * Get the "id" orderItemNotificationsStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderItemNotificationsStatusDTO> findOne(Long id);

    /**
     * Delete the "id" orderItemNotificationsStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the orderItemNotificationsStatus corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderItemNotificationsStatusDTO> search(String query, Pageable pageable);
}
