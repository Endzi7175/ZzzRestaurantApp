package com.ktsnwt.restaurant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ktsnwt.restaurant.IntegrationTest;
import com.ktsnwt.restaurant.domain.RestaurantTable;
import com.ktsnwt.restaurant.repository.RestaurantTableRepository;
import com.ktsnwt.restaurant.repository.search.RestaurantTableSearchRepository;
import com.ktsnwt.restaurant.service.dto.RestaurantTableDTO;
import com.ktsnwt.restaurant.service.mapper.RestaurantTableMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RestaurantTableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RestaurantTableResourceIT {

    private static final Integer DEFAULT_TABLE_NO = 1;
    private static final Integer UPDATED_TABLE_NO = 2;

    private static final Integer DEFAULT_SEATS_NO = 1;
    private static final Integer UPDATED_SEATS_NO = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_X_POSITION_FROM = 1;
    private static final Integer UPDATED_X_POSITION_FROM = 2;

    private static final Integer DEFAULT_X_POSITION_TO = 1;
    private static final Integer UPDATED_X_POSITION_TO = 2;

    private static final Integer DEFAULT_Y_POSITION_FROM = 1;
    private static final Integer UPDATED_Y_POSITION_FROM = 2;

    private static final Integer DEFAULT_Y_POSITION_TO = 1;
    private static final Integer UPDATED_Y_POSITION_TO = 2;

    private static final String ENTITY_API_URL = "/api/restaurant-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/restaurant-tables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private RestaurantTableMapper restaurantTableMapper;

    /**
     * This repository is mocked in the com.ktsnwt.restaurant.repository.search test package.
     *
     * @see com.ktsnwt.restaurant.repository.search.RestaurantTableSearchRepositoryMockConfiguration
     */
    @Autowired
    private RestaurantTableSearchRepository mockRestaurantTableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantTableMockMvc;

    private RestaurantTable restaurantTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantTable createEntity(EntityManager em) {
        RestaurantTable restaurantTable = new RestaurantTable()
            .tableNo(DEFAULT_TABLE_NO)
            .seatsNo(DEFAULT_SEATS_NO)
            .description(DEFAULT_DESCRIPTION)
            .xPositionFrom(DEFAULT_X_POSITION_FROM)
            .xPositionTo(DEFAULT_X_POSITION_TO)
            .yPositionFrom(DEFAULT_Y_POSITION_FROM)
            .yPositionTo(DEFAULT_Y_POSITION_TO);
        return restaurantTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantTable createUpdatedEntity(EntityManager em) {
        RestaurantTable restaurantTable = new RestaurantTable()
            .tableNo(UPDATED_TABLE_NO)
            .seatsNo(UPDATED_SEATS_NO)
            .description(UPDATED_DESCRIPTION)
            .xPositionFrom(UPDATED_X_POSITION_FROM)
            .xPositionTo(UPDATED_X_POSITION_TO)
            .yPositionFrom(UPDATED_Y_POSITION_FROM)
            .yPositionTo(UPDATED_Y_POSITION_TO);
        return restaurantTable;
    }

    @BeforeEach
    public void initTest() {
        restaurantTable = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurantTable() throws Exception {
        int databaseSizeBeforeCreate = restaurantTableRepository.findAll().size();
        // Create the RestaurantTable
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);
        restRestaurantTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantTable testRestaurantTable = restaurantTableList.get(restaurantTableList.size() - 1);
        assertThat(testRestaurantTable.getTableNo()).isEqualTo(DEFAULT_TABLE_NO);
        assertThat(testRestaurantTable.getSeatsNo()).isEqualTo(DEFAULT_SEATS_NO);
        assertThat(testRestaurantTable.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestaurantTable.getxPositionFrom()).isEqualTo(DEFAULT_X_POSITION_FROM);
        assertThat(testRestaurantTable.getxPositionTo()).isEqualTo(DEFAULT_X_POSITION_TO);
        assertThat(testRestaurantTable.getyPositionFrom()).isEqualTo(DEFAULT_Y_POSITION_FROM);
        assertThat(testRestaurantTable.getyPositionTo()).isEqualTo(DEFAULT_Y_POSITION_TO);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(1)).save(testRestaurantTable);
    }

    @Test
    @Transactional
    void createRestaurantTableWithExistingId() throws Exception {
        // Create the RestaurantTable with an existing ID
        restaurantTable.setId(1L);
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);

        int databaseSizeBeforeCreate = restaurantTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeCreate);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(0)).save(restaurantTable);
    }

    @Test
    @Transactional
    void getAllRestaurantTables() throws Exception {
        // Initialize the database
        restaurantTableRepository.saveAndFlush(restaurantTable);

        // Get all the restaurantTableList
        restRestaurantTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableNo").value(hasItem(DEFAULT_TABLE_NO)))
            .andExpect(jsonPath("$.[*].seatsNo").value(hasItem(DEFAULT_SEATS_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].xPositionFrom").value(hasItem(DEFAULT_X_POSITION_FROM)))
            .andExpect(jsonPath("$.[*].xPositionTo").value(hasItem(DEFAULT_X_POSITION_TO)))
            .andExpect(jsonPath("$.[*].yPositionFrom").value(hasItem(DEFAULT_Y_POSITION_FROM)))
            .andExpect(jsonPath("$.[*].yPositionTo").value(hasItem(DEFAULT_Y_POSITION_TO)));
    }

    @Test
    @Transactional
    void getRestaurantTable() throws Exception {
        // Initialize the database
        restaurantTableRepository.saveAndFlush(restaurantTable);

        // Get the restaurantTable
        restRestaurantTableMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurantTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantTable.getId().intValue()))
            .andExpect(jsonPath("$.tableNo").value(DEFAULT_TABLE_NO))
            .andExpect(jsonPath("$.seatsNo").value(DEFAULT_SEATS_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.xPositionFrom").value(DEFAULT_X_POSITION_FROM))
            .andExpect(jsonPath("$.xPositionTo").value(DEFAULT_X_POSITION_TO))
            .andExpect(jsonPath("$.yPositionFrom").value(DEFAULT_Y_POSITION_FROM))
            .andExpect(jsonPath("$.yPositionTo").value(DEFAULT_Y_POSITION_TO));
    }

    @Test
    @Transactional
    void getNonExistingRestaurantTable() throws Exception {
        // Get the restaurantTable
        restRestaurantTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurantTable() throws Exception {
        // Initialize the database
        restaurantTableRepository.saveAndFlush(restaurantTable);

        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();

        // Update the restaurantTable
        RestaurantTable updatedRestaurantTable = restaurantTableRepository.findById(restaurantTable.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurantTable are not directly saved in db
        em.detach(updatedRestaurantTable);
        updatedRestaurantTable
            .tableNo(UPDATED_TABLE_NO)
            .seatsNo(UPDATED_SEATS_NO)
            .description(UPDATED_DESCRIPTION)
            .xPositionFrom(UPDATED_X_POSITION_FROM)
            .xPositionTo(UPDATED_X_POSITION_TO)
            .yPositionFrom(UPDATED_Y_POSITION_FROM)
            .yPositionTo(UPDATED_Y_POSITION_TO);
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(updatedRestaurantTable);

        restRestaurantTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);
        RestaurantTable testRestaurantTable = restaurantTableList.get(restaurantTableList.size() - 1);
        assertThat(testRestaurantTable.getTableNo()).isEqualTo(UPDATED_TABLE_NO);
        assertThat(testRestaurantTable.getSeatsNo()).isEqualTo(UPDATED_SEATS_NO);
        assertThat(testRestaurantTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestaurantTable.getxPositionFrom()).isEqualTo(UPDATED_X_POSITION_FROM);
        assertThat(testRestaurantTable.getxPositionTo()).isEqualTo(UPDATED_X_POSITION_TO);
        assertThat(testRestaurantTable.getyPositionFrom()).isEqualTo(UPDATED_Y_POSITION_FROM);
        assertThat(testRestaurantTable.getyPositionTo()).isEqualTo(UPDATED_Y_POSITION_TO);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository).save(testRestaurantTable);
    }

    @Test
    @Transactional
    void putNonExistingRestaurantTable() throws Exception {
        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();
        restaurantTable.setId(count.incrementAndGet());

        // Create the RestaurantTable
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(0)).save(restaurantTable);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurantTable() throws Exception {
        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();
        restaurantTable.setId(count.incrementAndGet());

        // Create the RestaurantTable
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(0)).save(restaurantTable);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurantTable() throws Exception {
        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();
        restaurantTable.setId(count.incrementAndGet());

        // Create the RestaurantTable
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantTableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(0)).save(restaurantTable);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantTableWithPatch() throws Exception {
        // Initialize the database
        restaurantTableRepository.saveAndFlush(restaurantTable);

        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();

        // Update the restaurantTable using partial update
        RestaurantTable partialUpdatedRestaurantTable = new RestaurantTable();
        partialUpdatedRestaurantTable.setId(restaurantTable.getId());

        partialUpdatedRestaurantTable.tableNo(UPDATED_TABLE_NO).seatsNo(UPDATED_SEATS_NO);

        restRestaurantTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantTable))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);
        RestaurantTable testRestaurantTable = restaurantTableList.get(restaurantTableList.size() - 1);
        assertThat(testRestaurantTable.getTableNo()).isEqualTo(UPDATED_TABLE_NO);
        assertThat(testRestaurantTable.getSeatsNo()).isEqualTo(UPDATED_SEATS_NO);
        assertThat(testRestaurantTable.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestaurantTable.getxPositionFrom()).isEqualTo(DEFAULT_X_POSITION_FROM);
        assertThat(testRestaurantTable.getxPositionTo()).isEqualTo(DEFAULT_X_POSITION_TO);
        assertThat(testRestaurantTable.getyPositionFrom()).isEqualTo(DEFAULT_Y_POSITION_FROM);
        assertThat(testRestaurantTable.getyPositionTo()).isEqualTo(DEFAULT_Y_POSITION_TO);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantTableWithPatch() throws Exception {
        // Initialize the database
        restaurantTableRepository.saveAndFlush(restaurantTable);

        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();

        // Update the restaurantTable using partial update
        RestaurantTable partialUpdatedRestaurantTable = new RestaurantTable();
        partialUpdatedRestaurantTable.setId(restaurantTable.getId());

        partialUpdatedRestaurantTable
            .tableNo(UPDATED_TABLE_NO)
            .seatsNo(UPDATED_SEATS_NO)
            .description(UPDATED_DESCRIPTION)
            .xPositionFrom(UPDATED_X_POSITION_FROM)
            .xPositionTo(UPDATED_X_POSITION_TO)
            .yPositionFrom(UPDATED_Y_POSITION_FROM)
            .yPositionTo(UPDATED_Y_POSITION_TO);

        restRestaurantTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantTable))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);
        RestaurantTable testRestaurantTable = restaurantTableList.get(restaurantTableList.size() - 1);
        assertThat(testRestaurantTable.getTableNo()).isEqualTo(UPDATED_TABLE_NO);
        assertThat(testRestaurantTable.getSeatsNo()).isEqualTo(UPDATED_SEATS_NO);
        assertThat(testRestaurantTable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestaurantTable.getxPositionFrom()).isEqualTo(UPDATED_X_POSITION_FROM);
        assertThat(testRestaurantTable.getxPositionTo()).isEqualTo(UPDATED_X_POSITION_TO);
        assertThat(testRestaurantTable.getyPositionFrom()).isEqualTo(UPDATED_Y_POSITION_FROM);
        assertThat(testRestaurantTable.getyPositionTo()).isEqualTo(UPDATED_Y_POSITION_TO);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurantTable() throws Exception {
        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();
        restaurantTable.setId(count.incrementAndGet());

        // Create the RestaurantTable
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantTableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(0)).save(restaurantTable);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurantTable() throws Exception {
        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();
        restaurantTable.setId(count.incrementAndGet());

        // Create the RestaurantTable
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(0)).save(restaurantTable);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurantTable() throws Exception {
        int databaseSizeBeforeUpdate = restaurantTableRepository.findAll().size();
        restaurantTable.setId(count.incrementAndGet());

        // Create the RestaurantTable
        RestaurantTableDTO restaurantTableDTO = restaurantTableMapper.toDto(restaurantTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantTableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantTable in the database
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(0)).save(restaurantTable);
    }

    @Test
    @Transactional
    void deleteRestaurantTable() throws Exception {
        // Initialize the database
        restaurantTableRepository.saveAndFlush(restaurantTable);

        int databaseSizeBeforeDelete = restaurantTableRepository.findAll().size();

        // Delete the restaurantTable
        restRestaurantTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurantTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAll();
        assertThat(restaurantTableList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RestaurantTable in Elasticsearch
        verify(mockRestaurantTableSearchRepository, times(1)).deleteById(restaurantTable.getId());
    }

    @Test
    @Transactional
    void searchRestaurantTable() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        restaurantTableRepository.saveAndFlush(restaurantTable);
        when(mockRestaurantTableSearchRepository.search("id:" + restaurantTable.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(restaurantTable), PageRequest.of(0, 1), 1));

        // Search the restaurantTable
        restRestaurantTableMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + restaurantTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableNo").value(hasItem(DEFAULT_TABLE_NO)))
            .andExpect(jsonPath("$.[*].seatsNo").value(hasItem(DEFAULT_SEATS_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].xPositionFrom").value(hasItem(DEFAULT_X_POSITION_FROM)))
            .andExpect(jsonPath("$.[*].xPositionTo").value(hasItem(DEFAULT_X_POSITION_TO)))
            .andExpect(jsonPath("$.[*].yPositionFrom").value(hasItem(DEFAULT_Y_POSITION_FROM)))
            .andExpect(jsonPath("$.[*].yPositionTo").value(hasItem(DEFAULT_Y_POSITION_TO)));
    }
}
