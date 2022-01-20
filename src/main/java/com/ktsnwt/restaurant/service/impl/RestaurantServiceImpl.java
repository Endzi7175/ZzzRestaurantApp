package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.Restaurant;
import com.ktsnwt.restaurant.repository.RestaurantRepository;
import com.ktsnwt.restaurant.repository.search.RestaurantSearchRepository;
import com.ktsnwt.restaurant.service.RestaurantService;
import com.ktsnwt.restaurant.service.dto.RestaurantDTO;
import com.ktsnwt.restaurant.service.mapper.RestaurantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    private final RestaurantSearchRepository restaurantSearchRepository;

    public RestaurantServiceImpl(
        RestaurantRepository restaurantRepository,
        RestaurantMapper restaurantMapper,
        RestaurantSearchRepository restaurantSearchRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.restaurantSearchRepository = restaurantSearchRepository;
    }

    @Override
    public RestaurantDTO save(RestaurantDTO restaurantDTO) {
        log.debug("Request to save Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        RestaurantDTO result = restaurantMapper.toDto(restaurant);
        restaurantSearchRepository.save(restaurant);
        return result;
    }

    @Override
    public Optional<RestaurantDTO> partialUpdate(RestaurantDTO restaurantDTO) {
        log.debug("Request to partially update Restaurant : {}", restaurantDTO);

        return restaurantRepository
            .findById(restaurantDTO.getId())
            .map(existingRestaurant -> {
                restaurantMapper.partialUpdate(existingRestaurant, restaurantDTO);

                return existingRestaurant;
            })
            .map(restaurantRepository::save)
            .map(savedRestaurant -> {
                restaurantSearchRepository.save(savedRestaurant);

                return savedRestaurant;
            })
            .map(restaurantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Restaurants");
        return restaurantRepository.findAll(pageable).map(restaurantMapper::toDto);
    }

    public Page<RestaurantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return restaurantRepository.findAllWithEagerRelationships(pageable).map(restaurantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantDTO> findOne(Long id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findOneWithEagerRelationships(id).map(restaurantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
        restaurantSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Restaurants for query {}", query);
        return restaurantSearchRepository.search(query, pageable).map(restaurantMapper::toDto);
    }
}
