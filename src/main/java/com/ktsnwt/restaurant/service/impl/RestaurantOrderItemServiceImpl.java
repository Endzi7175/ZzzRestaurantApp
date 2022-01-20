package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.RestaurantOrderItem;
import com.ktsnwt.restaurant.repository.RestaurantOrderItemRepository;
import com.ktsnwt.restaurant.repository.search.RestaurantOrderItemSearchRepository;
import com.ktsnwt.restaurant.service.RestaurantOrderItemService;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderItemDTO;
import com.ktsnwt.restaurant.service.mapper.RestaurantOrderItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RestaurantOrderItem}.
 */
@Service
@Transactional
public class RestaurantOrderItemServiceImpl implements RestaurantOrderItemService {

    private final Logger log = LoggerFactory.getLogger(RestaurantOrderItemServiceImpl.class);

    private final RestaurantOrderItemRepository restaurantOrderItemRepository;

    private final RestaurantOrderItemMapper restaurantOrderItemMapper;

    private final RestaurantOrderItemSearchRepository restaurantOrderItemSearchRepository;

    public RestaurantOrderItemServiceImpl(
        RestaurantOrderItemRepository restaurantOrderItemRepository,
        RestaurantOrderItemMapper restaurantOrderItemMapper,
        RestaurantOrderItemSearchRepository restaurantOrderItemSearchRepository
    ) {
        this.restaurantOrderItemRepository = restaurantOrderItemRepository;
        this.restaurantOrderItemMapper = restaurantOrderItemMapper;
        this.restaurantOrderItemSearchRepository = restaurantOrderItemSearchRepository;
    }

    @Override
    public RestaurantOrderItemDTO save(RestaurantOrderItemDTO restaurantOrderItemDTO) {
        log.debug("Request to save RestaurantOrderItem : {}", restaurantOrderItemDTO);
        RestaurantOrderItem restaurantOrderItem = restaurantOrderItemMapper.toEntity(restaurantOrderItemDTO);
        restaurantOrderItem = restaurantOrderItemRepository.save(restaurantOrderItem);
        RestaurantOrderItemDTO result = restaurantOrderItemMapper.toDto(restaurantOrderItem);
        restaurantOrderItemSearchRepository.save(restaurantOrderItem);
        return result;
    }

    @Override
    public Optional<RestaurantOrderItemDTO> partialUpdate(RestaurantOrderItemDTO restaurantOrderItemDTO) {
        log.debug("Request to partially update RestaurantOrderItem : {}", restaurantOrderItemDTO);

        return restaurantOrderItemRepository
            .findById(restaurantOrderItemDTO.getId())
            .map(existingRestaurantOrderItem -> {
                restaurantOrderItemMapper.partialUpdate(existingRestaurantOrderItem, restaurantOrderItemDTO);

                return existingRestaurantOrderItem;
            })
            .map(restaurantOrderItemRepository::save)
            .map(savedRestaurantOrderItem -> {
                restaurantOrderItemSearchRepository.save(savedRestaurantOrderItem);

                return savedRestaurantOrderItem;
            })
            .map(restaurantOrderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantOrderItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RestaurantOrderItems");
        return restaurantOrderItemRepository.findAll(pageable).map(restaurantOrderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantOrderItemDTO> findOne(Long id) {
        log.debug("Request to get RestaurantOrderItem : {}", id);
        return restaurantOrderItemRepository.findById(id).map(restaurantOrderItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RestaurantOrderItem : {}", id);
        restaurantOrderItemRepository.deleteById(id);
        restaurantOrderItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantOrderItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RestaurantOrderItems for query {}", query);
        return restaurantOrderItemSearchRepository.search(query, pageable).map(restaurantOrderItemMapper::toDto);
    }
}
