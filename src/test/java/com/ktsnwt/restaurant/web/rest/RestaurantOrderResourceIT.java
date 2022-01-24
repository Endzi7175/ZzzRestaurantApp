package com.ktsnwt.restaurant.web.rest;

import static com.ktsnwt.restaurant.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ktsnwt.restaurant.IntegrationTest;
import com.ktsnwt.restaurant.domain.RestaurantOrder;
import com.ktsnwt.restaurant.repository.RestaurantOrderRepository;
import com.ktsnwt.restaurant.repository.search.RestaurantOrderSearchRepository;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderDTO;
import com.ktsnwt.restaurant.service.mapper.RestaurantOrderMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link RestaurantOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RestaurantOrderResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_PRICE_EXCLUDING_TAX = 1F;
    private static final Float UPDATED_PRICE_EXCLUDING_TAX = 2F;

    private static final Float DEFAULT_PRICE_INCLUDING_TAX = 1F;
    private static final Float UPDATED_PRICE_INCLUDING_TAX = 2F;

    private static final String ENTITY_API_URL = "/api/restaurant-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/restaurant-orders";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantOrderRepository restaurantOrderRepository;

    @Autowired
    private RestaurantOrderMapper restaurantOrderMapper;

    /**
     * This repository is mocked in the com.ktsnwt.restaurant.repository.search test package.
     *
     * @see com.ktsnwt.restaurant.repository.search.RestaurantOrderSearchRepositoryMockConfiguration
     */
    @Autowired
    private RestaurantOrderSearchRepository mockRestaurantOrderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantOrderMockMvc;

    private RestaurantOrder restaurantOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantOrder createEntity(EntityManager em) {
        RestaurantOrder restaurantOrder = new RestaurantOrder()
            .date(DEFAULT_DATE)
            .priceExcludingTax(DEFAULT_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(DEFAULT_PRICE_INCLUDING_TAX);
        return restaurantOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantOrder createUpdatedEntity(EntityManager em) {
        RestaurantOrder restaurantOrder = new RestaurantOrder()
            .date(UPDATED_DATE)
            .priceExcludingTax(UPDATED_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX);
        return restaurantOrder;
    }

    @BeforeEach
    public void initTest() {
        restaurantOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurantOrder() throws Exception {
        int databaseSizeBeforeCreate = restaurantOrderRepository.findAll().size();
        // Create the RestaurantOrder
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);
        restRestaurantOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantOrder testRestaurantOrder = restaurantOrderList.get(restaurantOrderList.size() - 1);
        assertThat(testRestaurantOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRestaurantOrder.getPriceExcludingTax()).isEqualTo(DEFAULT_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrder.getPriceIncludingTax()).isEqualTo(DEFAULT_PRICE_INCLUDING_TAX);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(1)).save(testRestaurantOrder);
    }

    @Test
    @Transactional
    void createRestaurantOrderWithExistingId() throws Exception {
        // Create the RestaurantOrder with an existing ID
        restaurantOrder.setId(1L);
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);

        int databaseSizeBeforeCreate = restaurantOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeCreate);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(0)).save(restaurantOrder);
    }

    @Test
    @Transactional
    void getAllRestaurantOrders() throws Exception {
        // Initialize the database
        restaurantOrderRepository.saveAndFlush(restaurantOrder);

        // Get all the restaurantOrderList
        restRestaurantOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].priceExcludingTax").value(hasItem(DEFAULT_PRICE_EXCLUDING_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].priceIncludingTax").value(hasItem(DEFAULT_PRICE_INCLUDING_TAX.doubleValue())));
    }

    @Test
    @Transactional
    void getRestaurantOrder() throws Exception {
        // Initialize the database
        restaurantOrderRepository.saveAndFlush(restaurantOrder);

        // Get the restaurantOrder
        restRestaurantOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurantOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantOrder.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.priceExcludingTax").value(DEFAULT_PRICE_EXCLUDING_TAX.doubleValue()))
            .andExpect(jsonPath("$.priceIncludingTax").value(DEFAULT_PRICE_INCLUDING_TAX.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingRestaurantOrder() throws Exception {
        // Get the restaurantOrder
        restRestaurantOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurantOrder() throws Exception {
        // Initialize the database
        restaurantOrderRepository.saveAndFlush(restaurantOrder);

        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();

        // Update the restaurantOrder
        RestaurantOrder updatedRestaurantOrder = restaurantOrderRepository.findById(restaurantOrder.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurantOrder are not directly saved in db
        em.detach(updatedRestaurantOrder);
        updatedRestaurantOrder
            .date(UPDATED_DATE)
            .priceExcludingTax(UPDATED_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX);
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(updatedRestaurantOrder);

        restRestaurantOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);
        RestaurantOrder testRestaurantOrder = restaurantOrderList.get(restaurantOrderList.size() - 1);
        assertThat(testRestaurantOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRestaurantOrder.getPriceExcludingTax()).isEqualTo(UPDATED_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrder.getPriceIncludingTax()).isEqualTo(UPDATED_PRICE_INCLUDING_TAX);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository).save(testRestaurantOrder);
    }

    @Test
    @Transactional
    void putNonExistingRestaurantOrder() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();
        restaurantOrder.setId(count.incrementAndGet());

        // Create the RestaurantOrder
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(0)).save(restaurantOrder);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurantOrder() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();
        restaurantOrder.setId(count.incrementAndGet());

        // Create the RestaurantOrder
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(0)).save(restaurantOrder);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurantOrder() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();
        restaurantOrder.setId(count.incrementAndGet());

        // Create the RestaurantOrder
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(0)).save(restaurantOrder);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantOrderWithPatch() throws Exception {
        // Initialize the database
        restaurantOrderRepository.saveAndFlush(restaurantOrder);

        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();

        // Update the restaurantOrder using partial update
        RestaurantOrder partialUpdatedRestaurantOrder = new RestaurantOrder();
        partialUpdatedRestaurantOrder.setId(restaurantOrder.getId());

        partialUpdatedRestaurantOrder.date(UPDATED_DATE).priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX);

        restRestaurantOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantOrder))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);
        RestaurantOrder testRestaurantOrder = restaurantOrderList.get(restaurantOrderList.size() - 1);
        assertThat(testRestaurantOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRestaurantOrder.getPriceExcludingTax()).isEqualTo(DEFAULT_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrder.getPriceIncludingTax()).isEqualTo(UPDATED_PRICE_INCLUDING_TAX);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantOrderWithPatch() throws Exception {
        // Initialize the database
        restaurantOrderRepository.saveAndFlush(restaurantOrder);

        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();

        // Update the restaurantOrder using partial update
        RestaurantOrder partialUpdatedRestaurantOrder = new RestaurantOrder();
        partialUpdatedRestaurantOrder.setId(restaurantOrder.getId());

        partialUpdatedRestaurantOrder
            .date(UPDATED_DATE)
            .priceExcludingTax(UPDATED_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX);

        restRestaurantOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantOrder))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);
        RestaurantOrder testRestaurantOrder = restaurantOrderList.get(restaurantOrderList.size() - 1);
        assertThat(testRestaurantOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRestaurantOrder.getPriceExcludingTax()).isEqualTo(UPDATED_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrder.getPriceIncludingTax()).isEqualTo(UPDATED_PRICE_INCLUDING_TAX);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurantOrder() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();
        restaurantOrder.setId(count.incrementAndGet());

        // Create the RestaurantOrder
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(0)).save(restaurantOrder);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurantOrder() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();
        restaurantOrder.setId(count.incrementAndGet());

        // Create the RestaurantOrder
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(0)).save(restaurantOrder);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurantOrder() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderRepository.findAll().size();
        restaurantOrder.setId(count.incrementAndGet());

        // Create the RestaurantOrder
        RestaurantOrderDTO restaurantOrderDTO = restaurantOrderMapper.toDto(restaurantOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantOrder in the database
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(0)).save(restaurantOrder);
    }

    @Test
    @Transactional
    void deleteRestaurantOrder() throws Exception {
        // Initialize the database
        restaurantOrderRepository.saveAndFlush(restaurantOrder);

        int databaseSizeBeforeDelete = restaurantOrderRepository.findAll().size();

        // Delete the restaurantOrder
        restRestaurantOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurantOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RestaurantOrder> restaurantOrderList = restaurantOrderRepository.findAll();
        assertThat(restaurantOrderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RestaurantOrder in Elasticsearch
        verify(mockRestaurantOrderSearchRepository, times(1)).deleteById(restaurantOrder.getId());
    }

    @Test
    @Transactional
    void searchRestaurantOrder() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        restaurantOrderRepository.saveAndFlush(restaurantOrder);
        when(mockRestaurantOrderSearchRepository.search("id:" + restaurantOrder.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(restaurantOrder), PageRequest.of(0, 1), 1));

        // Search the restaurantOrder
        restRestaurantOrderMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + restaurantOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].priceExcludingTax").value(hasItem(DEFAULT_PRICE_EXCLUDING_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].priceIncludingTax").value(hasItem(DEFAULT_PRICE_INCLUDING_TAX.doubleValue())));
    }
}
