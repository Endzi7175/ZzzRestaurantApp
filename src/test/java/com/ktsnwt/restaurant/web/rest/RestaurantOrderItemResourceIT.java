package com.ktsnwt.restaurant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ktsnwt.restaurant.IntegrationTest;
import com.ktsnwt.restaurant.domain.RestaurantOrderItem;
import com.ktsnwt.restaurant.domain.enumeration.OrderItemStatus;
import com.ktsnwt.restaurant.repository.RestaurantOrderItemRepository;
import com.ktsnwt.restaurant.repository.search.RestaurantOrderItemSearchRepository;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderItemDTO;
import com.ktsnwt.restaurant.service.mapper.RestaurantOrderItemMapper;
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
 * Integration tests for the {@link RestaurantOrderItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RestaurantOrderItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_MENU_ITEM_ID = 1L;
    private static final Long UPDATED_MENU_ITEM_ID = 2L;

    private static final Float DEFAULT_PRICE_EXCLUDING_TAX = 1F;
    private static final Float UPDATED_PRICE_EXCLUDING_TAX = 2F;

    private static final Float DEFAULT_PRICE_INCLUDING_TAX = 1F;
    private static final Float UPDATED_PRICE_INCLUDING_TAX = 2F;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final OrderItemStatus DEFAULT_STATUS = OrderItemStatus.CREATED;
    private static final OrderItemStatus UPDATED_STATUS = OrderItemStatus.ACCEPTED;

    private static final String ENTITY_API_URL = "/api/restaurant-order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/restaurant-order-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantOrderItemRepository restaurantOrderItemRepository;

    @Autowired
    private RestaurantOrderItemMapper restaurantOrderItemMapper;

    /**
     * This repository is mocked in the com.ktsnwt.restaurant.repository.search test package.
     *
     * @see com.ktsnwt.restaurant.repository.search.RestaurantOrderItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private RestaurantOrderItemSearchRepository mockRestaurantOrderItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantOrderItemMockMvc;

    private RestaurantOrderItem restaurantOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantOrderItem createEntity(EntityManager em) {
        RestaurantOrderItem restaurantOrderItem = new RestaurantOrderItem()
            .name(DEFAULT_NAME)
            .menuItemId(DEFAULT_MENU_ITEM_ID)
            .priceExcludingTax(DEFAULT_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(DEFAULT_PRICE_INCLUDING_TAX)
            .quantity(DEFAULT_QUANTITY)
            .status(DEFAULT_STATUS);
        return restaurantOrderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantOrderItem createUpdatedEntity(EntityManager em) {
        RestaurantOrderItem restaurantOrderItem = new RestaurantOrderItem()
            .name(UPDATED_NAME)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .priceExcludingTax(UPDATED_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX)
            .quantity(UPDATED_QUANTITY)
            .status(UPDATED_STATUS);
        return restaurantOrderItem;
    }

    @BeforeEach
    public void initTest() {
        restaurantOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurantOrderItem() throws Exception {
        int databaseSizeBeforeCreate = restaurantOrderItemRepository.findAll().size();
        // Create the RestaurantOrderItem
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);
        restRestaurantOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantOrderItem testRestaurantOrderItem = restaurantOrderItemList.get(restaurantOrderItemList.size() - 1);
        assertThat(testRestaurantOrderItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurantOrderItem.getMenuItemId()).isEqualTo(DEFAULT_MENU_ITEM_ID);
        assertThat(testRestaurantOrderItem.getPriceExcludingTax()).isEqualTo(DEFAULT_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getPriceIncludingTax()).isEqualTo(DEFAULT_PRICE_INCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testRestaurantOrderItem.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(1)).save(testRestaurantOrderItem);
    }

    @Test
    @Transactional
    void createRestaurantOrderItemWithExistingId() throws Exception {
        // Create the RestaurantOrderItem with an existing ID
        restaurantOrderItem.setId(1L);
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);

        int databaseSizeBeforeCreate = restaurantOrderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(0)).save(restaurantOrderItem);
    }

    @Test
    @Transactional
    void getAllRestaurantOrderItems() throws Exception {
        // Initialize the database
        restaurantOrderItemRepository.saveAndFlush(restaurantOrderItem);

        // Get all the restaurantOrderItemList
        restRestaurantOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].menuItemId").value(hasItem(DEFAULT_MENU_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].priceExcludingTax").value(hasItem(DEFAULT_PRICE_EXCLUDING_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].priceIncludingTax").value(hasItem(DEFAULT_PRICE_INCLUDING_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getRestaurantOrderItem() throws Exception {
        // Initialize the database
        restaurantOrderItemRepository.saveAndFlush(restaurantOrderItem);

        // Get the restaurantOrderItem
        restRestaurantOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurantOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.menuItemId").value(DEFAULT_MENU_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.priceExcludingTax").value(DEFAULT_PRICE_EXCLUDING_TAX.doubleValue()))
            .andExpect(jsonPath("$.priceIncludingTax").value(DEFAULT_PRICE_INCLUDING_TAX.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRestaurantOrderItem() throws Exception {
        // Get the restaurantOrderItem
        restRestaurantOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurantOrderItem() throws Exception {
        // Initialize the database
        restaurantOrderItemRepository.saveAndFlush(restaurantOrderItem);

        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();

        // Update the restaurantOrderItem
        RestaurantOrderItem updatedRestaurantOrderItem = restaurantOrderItemRepository.findById(restaurantOrderItem.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurantOrderItem are not directly saved in db
        em.detach(updatedRestaurantOrderItem);
        updatedRestaurantOrderItem
            .name(UPDATED_NAME)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .priceExcludingTax(UPDATED_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX)
            .quantity(UPDATED_QUANTITY)
            .status(UPDATED_STATUS);
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(updatedRestaurantOrderItem);

        restRestaurantOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantOrderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);
        RestaurantOrderItem testRestaurantOrderItem = restaurantOrderItemList.get(restaurantOrderItemList.size() - 1);
        assertThat(testRestaurantOrderItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurantOrderItem.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testRestaurantOrderItem.getPriceExcludingTax()).isEqualTo(UPDATED_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getPriceIncludingTax()).isEqualTo(UPDATED_PRICE_INCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testRestaurantOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository).save(testRestaurantOrderItem);
    }

    @Test
    @Transactional
    void putNonExistingRestaurantOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();
        restaurantOrderItem.setId(count.incrementAndGet());

        // Create the RestaurantOrderItem
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantOrderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(0)).save(restaurantOrderItem);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurantOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();
        restaurantOrderItem.setId(count.incrementAndGet());

        // Create the RestaurantOrderItem
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(0)).save(restaurantOrderItem);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurantOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();
        restaurantOrderItem.setId(count.incrementAndGet());

        // Create the RestaurantOrderItem
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(0)).save(restaurantOrderItem);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantOrderItemWithPatch() throws Exception {
        // Initialize the database
        restaurantOrderItemRepository.saveAndFlush(restaurantOrderItem);

        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();

        // Update the restaurantOrderItem using partial update
        RestaurantOrderItem partialUpdatedRestaurantOrderItem = new RestaurantOrderItem();
        partialUpdatedRestaurantOrderItem.setId(restaurantOrderItem.getId());

        partialUpdatedRestaurantOrderItem
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .priceExcludingTax(UPDATED_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX)
            .quantity(UPDATED_QUANTITY);

        restRestaurantOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);
        RestaurantOrderItem testRestaurantOrderItem = restaurantOrderItemList.get(restaurantOrderItemList.size() - 1);
        assertThat(testRestaurantOrderItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurantOrderItem.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testRestaurantOrderItem.getPriceExcludingTax()).isEqualTo(UPDATED_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getPriceIncludingTax()).isEqualTo(UPDATED_PRICE_INCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testRestaurantOrderItem.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantOrderItemWithPatch() throws Exception {
        // Initialize the database
        restaurantOrderItemRepository.saveAndFlush(restaurantOrderItem);

        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();

        // Update the restaurantOrderItem using partial update
        RestaurantOrderItem partialUpdatedRestaurantOrderItem = new RestaurantOrderItem();
        partialUpdatedRestaurantOrderItem.setId(restaurantOrderItem.getId());

        partialUpdatedRestaurantOrderItem
            .name(UPDATED_NAME)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .priceExcludingTax(UPDATED_PRICE_EXCLUDING_TAX)
            .priceIncludingTax(UPDATED_PRICE_INCLUDING_TAX)
            .quantity(UPDATED_QUANTITY)
            .status(UPDATED_STATUS);

        restRestaurantOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);
        RestaurantOrderItem testRestaurantOrderItem = restaurantOrderItemList.get(restaurantOrderItemList.size() - 1);
        assertThat(testRestaurantOrderItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurantOrderItem.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testRestaurantOrderItem.getPriceExcludingTax()).isEqualTo(UPDATED_PRICE_EXCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getPriceIncludingTax()).isEqualTo(UPDATED_PRICE_INCLUDING_TAX);
        assertThat(testRestaurantOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testRestaurantOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurantOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();
        restaurantOrderItem.setId(count.incrementAndGet());

        // Create the RestaurantOrderItem
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantOrderItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(0)).save(restaurantOrderItem);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurantOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();
        restaurantOrderItem.setId(count.incrementAndGet());

        // Create the RestaurantOrderItem
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(0)).save(restaurantOrderItem);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurantOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = restaurantOrderItemRepository.findAll().size();
        restaurantOrderItem.setId(count.incrementAndGet());

        // Create the RestaurantOrderItem
        RestaurantOrderItemDTO restaurantOrderItemDTO = restaurantOrderItemMapper.toDto(restaurantOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantOrderItem in the database
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(0)).save(restaurantOrderItem);
    }

    @Test
    @Transactional
    void deleteRestaurantOrderItem() throws Exception {
        // Initialize the database
        restaurantOrderItemRepository.saveAndFlush(restaurantOrderItem);

        int databaseSizeBeforeDelete = restaurantOrderItemRepository.findAll().size();

        // Delete the restaurantOrderItem
        restRestaurantOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurantOrderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RestaurantOrderItem> restaurantOrderItemList = restaurantOrderItemRepository.findAll();
        assertThat(restaurantOrderItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RestaurantOrderItem in Elasticsearch
        verify(mockRestaurantOrderItemSearchRepository, times(1)).deleteById(restaurantOrderItem.getId());
    }

    @Test
    @Transactional
    void searchRestaurantOrderItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        restaurantOrderItemRepository.saveAndFlush(restaurantOrderItem);
        when(mockRestaurantOrderItemSearchRepository.search("id:" + restaurantOrderItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(restaurantOrderItem), PageRequest.of(0, 1), 1));

        // Search the restaurantOrderItem
        restRestaurantOrderItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + restaurantOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].menuItemId").value(hasItem(DEFAULT_MENU_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].priceExcludingTax").value(hasItem(DEFAULT_PRICE_EXCLUDING_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].priceIncludingTax").value(hasItem(DEFAULT_PRICE_INCLUDING_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
