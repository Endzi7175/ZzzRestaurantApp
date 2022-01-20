package com.ktsnwt.restaurant.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.repository.OrderItemNotificationsStatusRepository;
import com.ktsnwt.restaurant.service.OrderItemNotificationsStatusService;
import com.ktsnwt.restaurant.service.dto.OrderItemNotificationsStatusDTO;
import com.ktsnwt.restaurant.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.ktsnwt.restaurant.web.HeaderUtil;
import com.ktsnwt.restaurant.web.PaginationUtil;
import com.ktsnwt.restaurant.web.ResponseUtil;

/**
 * REST controller for managing {@link com.ktsnwt.restaurant.domain.OrderItemNotificationsStatus}.
 */
@RestController
@RequestMapping("/api")
public class OrderItemNotificationsStatusResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemNotificationsStatusResource.class);

    private static final String ENTITY_NAME = "orderItemNotificationsStatus";

    @Value("${application.clientApp.name}")
    private String applicationName;

    private final OrderItemNotificationsStatusService orderItemNotificationsStatusService;

    private final OrderItemNotificationsStatusRepository orderItemNotificationsStatusRepository;

    public OrderItemNotificationsStatusResource(
        OrderItemNotificationsStatusService orderItemNotificationsStatusService,
        OrderItemNotificationsStatusRepository orderItemNotificationsStatusRepository
    ) {
        this.orderItemNotificationsStatusService = orderItemNotificationsStatusService;
        this.orderItemNotificationsStatusRepository = orderItemNotificationsStatusRepository;
    }

    /**
     * {@code POST  /order-item-notifications-statuses} : Create a new orderItemNotificationsStatus.
     *
     * @param orderItemNotificationsStatusDTO the orderItemNotificationsStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderItemNotificationsStatusDTO, or with status {@code 400 (Bad Request)} if the orderItemNotificationsStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-item-notifications-statuses")
    public ResponseEntity<OrderItemNotificationsStatusDTO> createOrderItemNotificationsStatus(
        @RequestBody OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to save OrderItemNotificationsStatus : {}", orderItemNotificationsStatusDTO);
        if (orderItemNotificationsStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderItemNotificationsStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderItemNotificationsStatusDTO result = orderItemNotificationsStatusService.save(orderItemNotificationsStatusDTO);
        return ResponseEntity
            .created(new URI("/api/order-item-notifications-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-item-notifications-statuses/:id} : Updates an existing orderItemNotificationsStatus.
     *
     * @param id the id of the orderItemNotificationsStatusDTO to save.
     * @param orderItemNotificationsStatusDTO the orderItemNotificationsStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemNotificationsStatusDTO,
     * or with status {@code 400 (Bad Request)} if the orderItemNotificationsStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderItemNotificationsStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-item-notifications-statuses/{id}")
    public ResponseEntity<OrderItemNotificationsStatusDTO> updateOrderItemNotificationsStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderItemNotificationsStatus : {}, {}", id, orderItemNotificationsStatusDTO);
        if (orderItemNotificationsStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemNotificationsStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemNotificationsStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderItemNotificationsStatusDTO result = orderItemNotificationsStatusService.save(orderItemNotificationsStatusDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemNotificationsStatusDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /order-item-notifications-statuses/:id} : Partial updates given fields of an existing orderItemNotificationsStatus, field will ignore if it is null
     *
     * @param id the id of the orderItemNotificationsStatusDTO to save.
     * @param orderItemNotificationsStatusDTO the orderItemNotificationsStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderItemNotificationsStatusDTO,
     * or with status {@code 400 (Bad Request)} if the orderItemNotificationsStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderItemNotificationsStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderItemNotificationsStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-item-notifications-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemNotificationsStatusDTO> partialUpdateOrderItemNotificationsStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderItemNotificationsStatus partially : {}, {}", id, orderItemNotificationsStatusDTO);
        if (orderItemNotificationsStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemNotificationsStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderItemNotificationsStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderItemNotificationsStatusDTO> result = orderItemNotificationsStatusService.partialUpdate(
            orderItemNotificationsStatusDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderItemNotificationsStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-item-notifications-statuses} : get all the orderItemNotificationsStatuses.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderItemNotificationsStatuses in body.
     */
    @GetMapping("/order-item-notifications-statuses")
    public ResponseEntity<List<OrderItemNotificationsStatusDTO>> getAllOrderItemNotificationsStatuses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("orderitem-is-null".equals(filter)) {
            log.debug("REST request to get all OrderItemNotificationsStatuss where orderItem is null");
            return new ResponseEntity<>(orderItemNotificationsStatusService.findAllWhereOrderItemIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of OrderItemNotificationsStatuses");
        Page<OrderItemNotificationsStatusDTO> page = orderItemNotificationsStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-item-notifications-statuses/:id} : get the "id" orderItemNotificationsStatus.
     *
     * @param id the id of the orderItemNotificationsStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderItemNotificationsStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-item-notifications-statuses/{id}")
    public ResponseEntity<OrderItemNotificationsStatusDTO> getOrderItemNotificationsStatus(@PathVariable Long id) {
        log.debug("REST request to get OrderItemNotificationsStatus : {}", id);
        Optional<OrderItemNotificationsStatusDTO> orderItemNotificationsStatusDTO = orderItemNotificationsStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemNotificationsStatusDTO);
    }

    /**
     * {@code DELETE  /order-item-notifications-statuses/:id} : delete the "id" orderItemNotificationsStatus.
     *
     * @param id the id of the orderItemNotificationsStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-item-notifications-statuses/{id}")
    public ResponseEntity<Void> deleteOrderItemNotificationsStatus(@PathVariable Long id) {
        log.debug("REST request to delete OrderItemNotificationsStatus : {}", id);
        orderItemNotificationsStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/order-item-notifications-statuses?query=:query} : search for the orderItemNotificationsStatus corresponding
     * to the query.
     *
     * @param query the query of the orderItemNotificationsStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/order-item-notifications-statuses")
    public ResponseEntity<List<OrderItemNotificationsStatusDTO>> searchOrderItemNotificationsStatuses(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of OrderItemNotificationsStatuses for query {}", query);
        Page<OrderItemNotificationsStatusDTO> page = orderItemNotificationsStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
