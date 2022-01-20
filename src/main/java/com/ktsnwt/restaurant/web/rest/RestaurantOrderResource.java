package com.ktsnwt.restaurant.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.repository.RestaurantOrderRepository;
import com.ktsnwt.restaurant.service.RestaurantOrderService;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderDTO;
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
 * REST controller for managing {@link com.ktsnwt.restaurant.domain.RestaurantOrder}.
 */
@RestController
@RequestMapping("/api")
public class RestaurantOrderResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantOrderResource.class);

    private static final String ENTITY_NAME = "restaurantOrder";

    @Value("${application.clientApp.name}")
    private String applicationName;

    private final RestaurantOrderService restaurantOrderService;

    private final RestaurantOrderRepository restaurantOrderRepository;

    public RestaurantOrderResource(RestaurantOrderService restaurantOrderService, RestaurantOrderRepository restaurantOrderRepository) {
        this.restaurantOrderService = restaurantOrderService;
        this.restaurantOrderRepository = restaurantOrderRepository;
    }

    /**
     * {@code POST  /restaurant-orders} : Create a new restaurantOrder.
     *
     * @param restaurantOrderDTO the restaurantOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantOrderDTO, or with status {@code 400 (Bad Request)} if the restaurantOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurant-orders")
    public ResponseEntity<RestaurantOrderDTO> createRestaurantOrder(@RequestBody RestaurantOrderDTO restaurantOrderDTO)
        throws URISyntaxException {
        log.debug("REST request to save RestaurantOrder : {}", restaurantOrderDTO);
        if (restaurantOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new restaurantOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurantOrderDTO result = restaurantOrderService.save(restaurantOrderDTO);
        return ResponseEntity
            .created(new URI("/api/restaurant-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurant-orders/:id} : Updates an existing restaurantOrder.
     *
     * @param id the id of the restaurantOrderDTO to save.
     * @param restaurantOrderDTO the restaurantOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantOrderDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurant-orders/{id}")
    public ResponseEntity<RestaurantOrderDTO> updateRestaurantOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantOrderDTO restaurantOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RestaurantOrder : {}, {}", id, restaurantOrderDTO);
        if (restaurantOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestaurantOrderDTO result = restaurantOrderService.save(restaurantOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurant-orders/:id} : Partial updates given fields of an existing restaurantOrder, field will ignore if it is null
     *
     * @param id the id of the restaurantOrderDTO to save.
     * @param restaurantOrderDTO the restaurantOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantOrderDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restaurantOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurantOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurant-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestaurantOrderDTO> partialUpdateRestaurantOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantOrderDTO restaurantOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RestaurantOrder partially : {}, {}", id, restaurantOrderDTO);
        if (restaurantOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurantOrderDTO> result = restaurantOrderService.partialUpdate(restaurantOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurant-orders} : get all the restaurantOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantOrders in body.
     */
    @GetMapping("/restaurant-orders")
    public ResponseEntity<List<RestaurantOrderDTO>> getAllRestaurantOrders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of RestaurantOrders");
        Page<RestaurantOrderDTO> page = restaurantOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restaurant-orders/:id} : get the "id" restaurantOrder.
     *
     * @param id the id of the restaurantOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurant-orders/{id}")
    public ResponseEntity<RestaurantOrderDTO> getRestaurantOrder(@PathVariable Long id) {
        log.debug("REST request to get RestaurantOrder : {}", id);
        Optional<RestaurantOrderDTO> restaurantOrderDTO = restaurantOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurantOrderDTO);
    }

    /**
     * {@code DELETE  /restaurant-orders/:id} : delete the "id" restaurantOrder.
     *
     * @param id the id of the restaurantOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurant-orders/{id}")
    public ResponseEntity<Void> deleteRestaurantOrder(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantOrder : {}", id);
        restaurantOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/restaurant-orders?query=:query} : search for the restaurantOrder corresponding
     * to the query.
     *
     * @param query the query of the restaurantOrder search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/restaurant-orders")
    public ResponseEntity<List<RestaurantOrderDTO>> searchRestaurantOrders(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of RestaurantOrders for query {}", query);
        Page<RestaurantOrderDTO> page = restaurantOrderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
