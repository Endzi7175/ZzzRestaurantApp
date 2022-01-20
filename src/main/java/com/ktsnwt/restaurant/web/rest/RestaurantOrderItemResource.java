package com.ktsnwt.restaurant.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.repository.RestaurantOrderItemRepository;
import com.ktsnwt.restaurant.service.RestaurantOrderItemService;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderItemDTO;
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
 * REST controller for managing {@link com.ktsnwt.restaurant.domain.RestaurantOrderItem}.
 */
@RestController
@RequestMapping("/api")
public class RestaurantOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantOrderItemResource.class);

    private static final String ENTITY_NAME = "restaurantOrderItem";

    @Value("${application.clientApp.name}")
    private String applicationName;

    private final RestaurantOrderItemService restaurantOrderItemService;

    private final RestaurantOrderItemRepository restaurantOrderItemRepository;

    public RestaurantOrderItemResource(
        RestaurantOrderItemService restaurantOrderItemService,
        RestaurantOrderItemRepository restaurantOrderItemRepository
    ) {
        this.restaurantOrderItemService = restaurantOrderItemService;
        this.restaurantOrderItemRepository = restaurantOrderItemRepository;
    }

    /**
     * {@code POST  /restaurant-order-items} : Create a new restaurantOrderItem.
     *
     * @param restaurantOrderItemDTO the restaurantOrderItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantOrderItemDTO, or with status {@code 400 (Bad Request)} if the restaurantOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurant-order-items")
    public ResponseEntity<RestaurantOrderItemDTO> createRestaurantOrderItem(@RequestBody RestaurantOrderItemDTO restaurantOrderItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save RestaurantOrderItem : {}", restaurantOrderItemDTO);
        if (restaurantOrderItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new restaurantOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurantOrderItemDTO result = restaurantOrderItemService.save(restaurantOrderItemDTO);
        return ResponseEntity
            .created(new URI("/api/restaurant-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurant-order-items/:id} : Updates an existing restaurantOrderItem.
     *
     * @param id the id of the restaurantOrderItemDTO to save.
     * @param restaurantOrderItemDTO the restaurantOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantOrderItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurant-order-items/{id}")
    public ResponseEntity<RestaurantOrderItemDTO> updateRestaurantOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantOrderItemDTO restaurantOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RestaurantOrderItem : {}, {}", id, restaurantOrderItemDTO);
        if (restaurantOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestaurantOrderItemDTO result = restaurantOrderItemService.save(restaurantOrderItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantOrderItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurant-order-items/:id} : Partial updates given fields of an existing restaurantOrderItem, field will ignore if it is null
     *
     * @param id the id of the restaurantOrderItemDTO to save.
     * @param restaurantOrderItemDTO the restaurantOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantOrderItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restaurantOrderItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurantOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurant-order-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestaurantOrderItemDTO> partialUpdateRestaurantOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantOrderItemDTO restaurantOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RestaurantOrderItem partially : {}, {}", id, restaurantOrderItemDTO);
        if (restaurantOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurantOrderItemDTO> result = restaurantOrderItemService.partialUpdate(restaurantOrderItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantOrderItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurant-order-items} : get all the restaurantOrderItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantOrderItems in body.
     */
    @GetMapping("/restaurant-order-items")
    public ResponseEntity<List<RestaurantOrderItemDTO>> getAllRestaurantOrderItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of RestaurantOrderItems");
        Page<RestaurantOrderItemDTO> page = restaurantOrderItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restaurant-order-items/:id} : get the "id" restaurantOrderItem.
     *
     * @param id the id of the restaurantOrderItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantOrderItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurant-order-items/{id}")
    public ResponseEntity<RestaurantOrderItemDTO> getRestaurantOrderItem(@PathVariable Long id) {
        log.debug("REST request to get RestaurantOrderItem : {}", id);
        Optional<RestaurantOrderItemDTO> restaurantOrderItemDTO = restaurantOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurantOrderItemDTO);
    }

    /**
     * {@code DELETE  /restaurant-order-items/:id} : delete the "id" restaurantOrderItem.
     *
     * @param id the id of the restaurantOrderItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurant-order-items/{id}")
    public ResponseEntity<Void> deleteRestaurantOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantOrderItem : {}", id);
        restaurantOrderItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/restaurant-order-items?query=:query} : search for the restaurantOrderItem corresponding
     * to the query.
     *
     * @param query the query of the restaurantOrderItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/restaurant-order-items")
    public ResponseEntity<List<RestaurantOrderItemDTO>> searchRestaurantOrderItems(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of RestaurantOrderItems for query {}", query);
        Page<RestaurantOrderItemDTO> page = restaurantOrderItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
