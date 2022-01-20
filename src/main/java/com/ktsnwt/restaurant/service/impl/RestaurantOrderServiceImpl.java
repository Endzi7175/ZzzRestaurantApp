package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.RestaurantOrder;
import com.ktsnwt.restaurant.repository.RestaurantOrderRepository;
import com.ktsnwt.restaurant.repository.search.RestaurantOrderSearchRepository;
import com.ktsnwt.restaurant.service.RestaurantOrderService;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderDTO;
import com.ktsnwt.restaurant.service.mapper.RestaurantOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RestaurantOrder}.
 */
@Service
@Transactional
public class RestaurantOrderServiceImpl implements RestaurantOrderService {

    private final Logger log = LoggerFactory.getLogger(RestaurantOrderServiceImpl.class);

    private final RestaurantOrderRepository restaurantOrderRepository;

    private final RestaurantOrderMapper restaurantOrderMapper;

    private final RestaurantOrderSearchRepository restaurantOrderSearchRepository;

    public RestaurantOrderServiceImpl(
        RestaurantOrderRepository restaurantOrderRepository,
        RestaurantOrderMapper restaurantOrderMapper,
        RestaurantOrderSearchRepository restaurantOrderSearchRepository
    ) {
        this.restaurantOrderRepository = restaurantOrderRepository;
        this.restaurantOrderMapper = restaurantOrderMapper;
        this.restaurantOrderSearchRepository = restaurantOrderSearchRepository;
    }

    @Override
    public RestaurantOrderDTO save(RestaurantOrderDTO restaurantOrderDTO) {
        log.debug("Request to save RestaurantOrder : {}", restaurantOrderDTO);
        RestaurantOrder restaurantOrder = restaurantOrderMapper.toEntity(restaurantOrderDTO);
        restaurantOrder = restaurantOrderRepository.save(restaurantOrder);
        RestaurantOrderDTO result = restaurantOrderMapper.toDto(restaurantOrder);
        restaurantOrderSearchRepository.save(restaurantOrder);
        return result;
    }

    @Override
    public Optional<RestaurantOrderDTO> partialUpdate(RestaurantOrderDTO restaurantOrderDTO) {
        log.debug("Request to partially update RestaurantOrder : {}", restaurantOrderDTO);

        return restaurantOrderRepository
            .findById(restaurantOrderDTO.getId())
            .map(existingRestaurantOrder -> {
                restaurantOrderMapper.partialUpdate(existingRestaurantOrder, restaurantOrderDTO);

                return existingRestaurantOrder;
            })
            .map(restaurantOrderRepository::save)
            .map(savedRestaurantOrder -> {
                restaurantOrderSearchRepository.save(savedRestaurantOrder);

                return savedRestaurantOrder;
            })
            .map(restaurantOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RestaurantOrders");
        return restaurantOrderRepository.findAll(pageable).map(restaurantOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantOrderDTO> findOne(Long id) {
        log.debug("Request to get RestaurantOrder : {}", id);
        return restaurantOrderRepository.findById(id).map(restaurantOrderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RestaurantOrder : {}", id);
        restaurantOrderRepository.deleteById(id);
        restaurantOrderSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantOrderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RestaurantOrders for query {}", query);
        return restaurantOrderSearchRepository.search(query, pageable).map(restaurantOrderMapper::toDto);
    }
}
