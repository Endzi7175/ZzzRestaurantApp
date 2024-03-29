package com.ktsnwt.restaurant.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.repository.PictureRepository;
import com.ktsnwt.restaurant.service.PictureService;
import com.ktsnwt.restaurant.service.dto.PictureDTO;
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
 * REST controller for managing {@link com.ktsnwt.restaurant.domain.Picture}.
 */
@RestController
@RequestMapping("/api")
public class PictureResource {

    private final Logger log = LoggerFactory.getLogger(PictureResource.class);

    private static final String ENTITY_NAME = "picture";

    @Value("${application.clientApp.name}")
    private String applicationName;

    private final PictureService pictureService;

    private final PictureRepository pictureRepository;

    public PictureResource(PictureService pictureService, PictureRepository pictureRepository) {
        this.pictureService = pictureService;
        this.pictureRepository = pictureRepository;
    }

    /**
     * {@code POST  /pictures} : Create a new picture.
     *
     * @param pictureDTO the pictureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pictureDTO, or with status {@code 400 (Bad Request)} if the picture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pictures")
    public ResponseEntity<PictureDTO> createPicture(@RequestBody PictureDTO pictureDTO) throws URISyntaxException {
        log.debug("REST request to save Picture : {}", pictureDTO);
        if (pictureDTO.getId() != null) {
            throw new BadRequestAlertException("A new picture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PictureDTO result = pictureService.save(pictureDTO);
        return ResponseEntity
            .created(new URI("/api/pictures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pictures/:id} : Updates an existing picture.
     *
     * @param id the id of the pictureDTO to save.
     * @param pictureDTO the pictureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pictureDTO,
     * or with status {@code 400 (Bad Request)} if the pictureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pictureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pictures/{id}")
    public ResponseEntity<PictureDTO> updatePicture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PictureDTO pictureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Picture : {}, {}", id, pictureDTO);
        if (pictureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pictureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pictureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PictureDTO result = pictureService.save(pictureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pictureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pictures/:id} : Partial updates given fields of an existing picture, field will ignore if it is null
     *
     * @param id the id of the pictureDTO to save.
     * @param pictureDTO the pictureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pictureDTO,
     * or with status {@code 400 (Bad Request)} if the pictureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pictureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pictureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pictures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PictureDTO> partialUpdatePicture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PictureDTO pictureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Picture partially : {}, {}", id, pictureDTO);
        if (pictureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pictureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pictureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PictureDTO> result = pictureService.partialUpdate(pictureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pictureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pictures} : get all the pictures.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pictures in body.
     */
    @GetMapping("/pictures")
    public ResponseEntity<List<PictureDTO>> getAllPictures(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("menuitem-is-null".equals(filter)) {
            log.debug("REST request to get all Pictures where menuItem is null");
            return new ResponseEntity<>(pictureService.findAllWhereMenuItemIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Pictures");
        Page<PictureDTO> page = pictureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pictures/:id} : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pictureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pictures/{id}")
    public ResponseEntity<PictureDTO> getPicture(@PathVariable Long id) {
        log.debug("REST request to get Picture : {}", id);
        Optional<PictureDTO> pictureDTO = pictureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pictureDTO);
    }

    /**
     * {@code DELETE  /pictures/:id} : delete the "id" picture.
     *
     * @param id the id of the pictureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pictures/{id}")
    public ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        log.debug("REST request to delete Picture : {}", id);
        pictureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/pictures?query=:query} : search for the picture corresponding
     * to the query.
     *
     * @param query the query of the picture search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/pictures")
    public ResponseEntity<List<PictureDTO>> searchPictures(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Pictures for query {}", query);
        Page<PictureDTO> page = pictureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
