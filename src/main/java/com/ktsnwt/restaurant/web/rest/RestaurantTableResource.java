package com.ktsnwt.restaurant.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.repository.RestaurantTableRepository;
import com.ktsnwt.restaurant.service.RestaurantTableService;
import com.ktsnwt.restaurant.service.dto.RestaurantTableDTO;
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
 * REST controller for managing {@link com.ktsnwt.restaurant.domain.RestaurantTable}.
 */
@RestController
@RequestMapping("/api")
public class RestaurantTableResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantTableResource.class);

    private static final String ENTITY_NAME = "restaurantTable";

    @Value("${application.clientApp.name}")
    private String applicationName;

    private final RestaurantTableService restaurantTableService;

    private final RestaurantTableRepository restaurantTableRepository;

    public RestaurantTableResource(RestaurantTableService restaurantTableService, RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableService = restaurantTableService;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    /**
     * {@code POST  /restaurant-tables} : Create a new restaurantTable.
     *
     * @param restaurantTableDTO the restaurantTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantTableDTO, or with status {@code 400 (Bad Request)} if the restaurantTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurant-tables")
    public ResponseEntity<RestaurantTableDTO> createRestaurantTable(@RequestBody RestaurantTableDTO restaurantTableDTO)
        throws URISyntaxException {
        log.debug("REST request to save RestaurantTable : {}", restaurantTableDTO);
        if (restaurantTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new restaurantTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurantTableDTO result = restaurantTableService.save(restaurantTableDTO);
        return ResponseEntity
            .created(new URI("/api/restaurant-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurant-tables/:id} : Updates an existing restaurantTable.
     *
     * @param id the id of the restaurantTableDTO to save.
     * @param restaurantTableDTO the restaurantTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantTableDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurant-tables/{id}")
    public ResponseEntity<RestaurantTableDTO> updateRestaurantTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantTableDTO restaurantTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RestaurantTable : {}, {}", id, restaurantTableDTO);
        if (restaurantTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestaurantTableDTO result = restaurantTableService.save(restaurantTableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurant-tables/:id} : Partial updates given fields of an existing restaurantTable, field will ignore if it is null
     *
     * @param id the id of the restaurantTableDTO to save.
     * @param restaurantTableDTO the restaurantTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantTableDTO,
     * or with status {@code 400 (Bad Request)} if the restaurantTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restaurantTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurantTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurant-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestaurantTableDTO> partialUpdateRestaurantTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantTableDTO restaurantTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RestaurantTable partially : {}, {}", id, restaurantTableDTO);
        if (restaurantTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurantTableDTO> result = restaurantTableService.partialUpdate(restaurantTableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantTableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurant-tables} : get all the restaurantTables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantTables in body.
     */
    @GetMapping("/restaurant-tables")
    public ResponseEntity<List<RestaurantTableDTO>> getAllRestaurantTables(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of RestaurantTables");
        Page<RestaurantTableDTO> page = restaurantTableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restaurant-tables/:id} : get the "id" restaurantTable.
     *
     * @param id the id of the restaurantTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurant-tables/{id}")
    public ResponseEntity<RestaurantTableDTO> getRestaurantTable(@PathVariable Long id) {
        log.debug("REST request to get RestaurantTable : {}", id);
        Optional<RestaurantTableDTO> restaurantTableDTO = restaurantTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurantTableDTO);
    }

    /**
     * {@code DELETE  /restaurant-tables/:id} : delete the "id" restaurantTable.
     *
     * @param id the id of the restaurantTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurant-tables/{id}")
    public ResponseEntity<Void> deleteRestaurantTable(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantTable : {}", id);
        restaurantTableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/restaurant-tables?query=:query} : search for the restaurantTable corresponding
     * to the query.
     *
     * @param query the query of the restaurantTable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/restaurant-tables")
    public ResponseEntity<List<RestaurantTableDTO>> searchRestaurantTables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of RestaurantTables for query {}", query);
        Page<RestaurantTableDTO> page = restaurantTableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
