package com.ktsnwt.restaurant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ktsnwt.restaurant.IntegrationTest;
import com.ktsnwt.restaurant.domain.OrderItemNotificationsStatus;
import com.ktsnwt.restaurant.repository.OrderItemNotificationsStatusRepository;
import com.ktsnwt.restaurant.repository.search.OrderItemNotificationsStatusSearchRepository;
import com.ktsnwt.restaurant.service.dto.OrderItemNotificationsStatusDTO;
import com.ktsnwt.restaurant.service.mapper.OrderItemNotificationsStatusMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link OrderItemNotificationsStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderItemNotificationsStatusResourceIT {

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACCEPTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCEPTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PREPARED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PREPARED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SERVED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SERVED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CANCELED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CANCELED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/order-item-notifications-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/order-item-notifications-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderItemNotificationsStatusRepository orderItemNotificationsStatusRepository;

    @Autowired
    private OrderItemNotificationsStatusMapper orderItemNotificationsStatusMapper;

    /**
     * This repository is mocked in the com.ktsnwt.restaurant.repository.search test package.
     *
     * @see com.ktsnwt.restaurant.repository.search.OrderItemNotificationsStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrderItemNotificationsStatusSearchRepository mockOrderItemNotificationsStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemNotificationsStatusMockMvc;

    private OrderItemNotificationsStatus orderItemNotificationsStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemNotificationsStatus createEntity(EntityManager em) {
        OrderItemNotificationsStatus orderItemNotificationsStatus = new OrderItemNotificationsStatus()
            .created(DEFAULT_CREATED)
            .accepted(DEFAULT_ACCEPTED)
            .prepared(DEFAULT_PREPARED)
            .served(DEFAULT_SERVED)
            .canceled(DEFAULT_CANCELED);
        return orderItemNotificationsStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemNotificationsStatus createUpdatedEntity(EntityManager em) {
        OrderItemNotificationsStatus orderItemNotificationsStatus = new OrderItemNotificationsStatus()
            .created(UPDATED_CREATED)
            .accepted(UPDATED_ACCEPTED)
            .prepared(UPDATED_PREPARED)
            .served(UPDATED_SERVED)
            .canceled(UPDATED_CANCELED);
        return orderItemNotificationsStatus;
    }

    @BeforeEach
    public void initTest() {
        orderItemNotificationsStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderItemNotificationsStatus() throws Exception {
        int databaseSizeBeforeCreate = orderItemNotificationsStatusRepository.findAll().size();
        // Create the OrderItemNotificationsStatus
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );
        restOrderItemNotificationsStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItemNotificationsStatus testOrderItemNotificationsStatus = orderItemNotificationsStatusList.get(
            orderItemNotificationsStatusList.size() - 1
        );
        assertThat(testOrderItemNotificationsStatus.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testOrderItemNotificationsStatus.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testOrderItemNotificationsStatus.getPrepared()).isEqualTo(DEFAULT_PREPARED);
        assertThat(testOrderItemNotificationsStatus.getServed()).isEqualTo(DEFAULT_SERVED);
        assertThat(testOrderItemNotificationsStatus.getCanceled()).isEqualTo(DEFAULT_CANCELED);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(1)).save(testOrderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void createOrderItemNotificationsStatusWithExistingId() throws Exception {
        // Create the OrderItemNotificationsStatus with an existing ID
        orderItemNotificationsStatus.setId(1L);
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );

        int databaseSizeBeforeCreate = orderItemNotificationsStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemNotificationsStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(0)).save(orderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void getAllOrderItemNotificationsStatuses() throws Exception {
        // Initialize the database
        orderItemNotificationsStatusRepository.saveAndFlush(orderItemNotificationsStatus);

        // Get all the orderItemNotificationsStatusList
        restOrderItemNotificationsStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemNotificationsStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.toString())))
            .andExpect(jsonPath("$.[*].prepared").value(hasItem(DEFAULT_PREPARED.toString())))
            .andExpect(jsonPath("$.[*].served").value(hasItem(DEFAULT_SERVED.toString())))
            .andExpect(jsonPath("$.[*].canceled").value(hasItem(DEFAULT_CANCELED.toString())));
    }

    @Test
    @Transactional
    void getOrderItemNotificationsStatus() throws Exception {
        // Initialize the database
        orderItemNotificationsStatusRepository.saveAndFlush(orderItemNotificationsStatus);

        // Get the orderItemNotificationsStatus
        restOrderItemNotificationsStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItemNotificationsStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItemNotificationsStatus.getId().intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.toString()))
            .andExpect(jsonPath("$.prepared").value(DEFAULT_PREPARED.toString()))
            .andExpect(jsonPath("$.served").value(DEFAULT_SERVED.toString()))
            .andExpect(jsonPath("$.canceled").value(DEFAULT_CANCELED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrderItemNotificationsStatus() throws Exception {
        // Get the orderItemNotificationsStatus
        restOrderItemNotificationsStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderItemNotificationsStatus() throws Exception {
        // Initialize the database
        orderItemNotificationsStatusRepository.saveAndFlush(orderItemNotificationsStatus);

        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();

        // Update the orderItemNotificationsStatus
        OrderItemNotificationsStatus updatedOrderItemNotificationsStatus = orderItemNotificationsStatusRepository
            .findById(orderItemNotificationsStatus.getId())
            .get();
        // Disconnect from session so that the updates on updatedOrderItemNotificationsStatus are not directly saved in db
        em.detach(updatedOrderItemNotificationsStatus);
        updatedOrderItemNotificationsStatus
            .created(UPDATED_CREATED)
            .accepted(UPDATED_ACCEPTED)
            .prepared(UPDATED_PREPARED)
            .served(UPDATED_SERVED)
            .canceled(UPDATED_CANCELED);
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            updatedOrderItemNotificationsStatus
        );

        restOrderItemNotificationsStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemNotificationsStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);
        OrderItemNotificationsStatus testOrderItemNotificationsStatus = orderItemNotificationsStatusList.get(
            orderItemNotificationsStatusList.size() - 1
        );
        assertThat(testOrderItemNotificationsStatus.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrderItemNotificationsStatus.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testOrderItemNotificationsStatus.getPrepared()).isEqualTo(UPDATED_PREPARED);
        assertThat(testOrderItemNotificationsStatus.getServed()).isEqualTo(UPDATED_SERVED);
        assertThat(testOrderItemNotificationsStatus.getCanceled()).isEqualTo(UPDATED_CANCELED);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository).save(testOrderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void putNonExistingOrderItemNotificationsStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();
        orderItemNotificationsStatus.setId(count.incrementAndGet());

        // Create the OrderItemNotificationsStatus
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemNotificationsStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemNotificationsStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(0)).save(orderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItemNotificationsStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();
        orderItemNotificationsStatus.setId(count.incrementAndGet());

        // Create the OrderItemNotificationsStatus
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemNotificationsStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(0)).save(orderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItemNotificationsStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();
        orderItemNotificationsStatus.setId(count.incrementAndGet());

        // Create the OrderItemNotificationsStatus
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemNotificationsStatusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(0)).save(orderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemNotificationsStatusWithPatch() throws Exception {
        // Initialize the database
        orderItemNotificationsStatusRepository.saveAndFlush(orderItemNotificationsStatus);

        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();

        // Update the orderItemNotificationsStatus using partial update
        OrderItemNotificationsStatus partialUpdatedOrderItemNotificationsStatus = new OrderItemNotificationsStatus();
        partialUpdatedOrderItemNotificationsStatus.setId(orderItemNotificationsStatus.getId());

        partialUpdatedOrderItemNotificationsStatus.created(UPDATED_CREATED).prepared(UPDATED_PREPARED).served(UPDATED_SERVED);

        restOrderItemNotificationsStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemNotificationsStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItemNotificationsStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);
        OrderItemNotificationsStatus testOrderItemNotificationsStatus = orderItemNotificationsStatusList.get(
            orderItemNotificationsStatusList.size() - 1
        );
        assertThat(testOrderItemNotificationsStatus.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrderItemNotificationsStatus.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testOrderItemNotificationsStatus.getPrepared()).isEqualTo(UPDATED_PREPARED);
        assertThat(testOrderItemNotificationsStatus.getServed()).isEqualTo(UPDATED_SERVED);
        assertThat(testOrderItemNotificationsStatus.getCanceled()).isEqualTo(DEFAULT_CANCELED);
    }

    @Test
    @Transactional
    void fullUpdateOrderItemNotificationsStatusWithPatch() throws Exception {
        // Initialize the database
        orderItemNotificationsStatusRepository.saveAndFlush(orderItemNotificationsStatus);

        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();

        // Update the orderItemNotificationsStatus using partial update
        OrderItemNotificationsStatus partialUpdatedOrderItemNotificationsStatus = new OrderItemNotificationsStatus();
        partialUpdatedOrderItemNotificationsStatus.setId(orderItemNotificationsStatus.getId());

        partialUpdatedOrderItemNotificationsStatus
            .created(UPDATED_CREATED)
            .accepted(UPDATED_ACCEPTED)
            .prepared(UPDATED_PREPARED)
            .served(UPDATED_SERVED)
            .canceled(UPDATED_CANCELED);

        restOrderItemNotificationsStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemNotificationsStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItemNotificationsStatus))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);
        OrderItemNotificationsStatus testOrderItemNotificationsStatus = orderItemNotificationsStatusList.get(
            orderItemNotificationsStatusList.size() - 1
        );
        assertThat(testOrderItemNotificationsStatus.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testOrderItemNotificationsStatus.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testOrderItemNotificationsStatus.getPrepared()).isEqualTo(UPDATED_PREPARED);
        assertThat(testOrderItemNotificationsStatus.getServed()).isEqualTo(UPDATED_SERVED);
        assertThat(testOrderItemNotificationsStatus.getCanceled()).isEqualTo(UPDATED_CANCELED);
    }

    @Test
    @Transactional
    void patchNonExistingOrderItemNotificationsStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();
        orderItemNotificationsStatus.setId(count.incrementAndGet());

        // Create the OrderItemNotificationsStatus
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemNotificationsStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemNotificationsStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(0)).save(orderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItemNotificationsStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();
        orderItemNotificationsStatus.setId(count.incrementAndGet());

        // Create the OrderItemNotificationsStatus
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemNotificationsStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(0)).save(orderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItemNotificationsStatus() throws Exception {
        int databaseSizeBeforeUpdate = orderItemNotificationsStatusRepository.findAll().size();
        orderItemNotificationsStatus.setId(count.incrementAndGet());

        // Create the OrderItemNotificationsStatus
        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = orderItemNotificationsStatusMapper.toDto(
            orderItemNotificationsStatus
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemNotificationsStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemNotificationsStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemNotificationsStatus in the database
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(0)).save(orderItemNotificationsStatus);
    }

    @Test
    @Transactional
    void deleteOrderItemNotificationsStatus() throws Exception {
        // Initialize the database
        orderItemNotificationsStatusRepository.saveAndFlush(orderItemNotificationsStatus);

        int databaseSizeBeforeDelete = orderItemNotificationsStatusRepository.findAll().size();

        // Delete the orderItemNotificationsStatus
        restOrderItemNotificationsStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItemNotificationsStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItemNotificationsStatus> orderItemNotificationsStatusList = orderItemNotificationsStatusRepository.findAll();
        assertThat(orderItemNotificationsStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrderItemNotificationsStatus in Elasticsearch
        verify(mockOrderItemNotificationsStatusSearchRepository, times(1)).deleteById(orderItemNotificationsStatus.getId());
    }

    @Test
    @Transactional
    void searchOrderItemNotificationsStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        orderItemNotificationsStatusRepository.saveAndFlush(orderItemNotificationsStatus);
        when(mockOrderItemNotificationsStatusSearchRepository.search("id:" + orderItemNotificationsStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orderItemNotificationsStatus), PageRequest.of(0, 1), 1));

        // Search the orderItemNotificationsStatus
        restOrderItemNotificationsStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + orderItemNotificationsStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemNotificationsStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.toString())))
            .andExpect(jsonPath("$.[*].prepared").value(hasItem(DEFAULT_PREPARED.toString())))
            .andExpect(jsonPath("$.[*].served").value(hasItem(DEFAULT_SERVED.toString())))
            .andExpect(jsonPath("$.[*].canceled").value(hasItem(DEFAULT_CANCELED.toString())));
    }
}
