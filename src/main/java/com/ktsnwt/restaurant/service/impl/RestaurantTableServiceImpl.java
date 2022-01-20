package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.RestaurantTable;
import com.ktsnwt.restaurant.repository.RestaurantTableRepository;
import com.ktsnwt.restaurant.repository.search.RestaurantTableSearchRepository;
import com.ktsnwt.restaurant.service.RestaurantTableService;
import com.ktsnwt.restaurant.service.dto.RestaurantTableDTO;
import com.ktsnwt.restaurant.service.mapper.RestaurantTableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RestaurantTable}.
 */
@Service
@Transactional
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final Logger log = LoggerFactory.getLogger(RestaurantTableServiceImpl.class);

    private final RestaurantTableRepository restaurantTableRepository;

    private final RestaurantTableMapper restaurantTableMapper;

    private final RestaurantTableSearchRepository restaurantTableSearchRepository;

    public RestaurantTableServiceImpl(
        RestaurantTableRepository restaurantTableRepository,
        RestaurantTableMapper restaurantTableMapper,
        RestaurantTableSearchRepository restaurantTableSearchRepository
    ) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.restaurantTableMapper = restaurantTableMapper;
        this.restaurantTableSearchRepository = restaurantTableSearchRepository;
    }

    @Override
    public RestaurantTableDTO save(RestaurantTableDTO restaurantTableDTO) {
        log.debug("Request to save RestaurantTable : {}", restaurantTableDTO);
        RestaurantTable restaurantTable = restaurantTableMapper.toEntity(restaurantTableDTO);
        restaurantTable = restaurantTableRepository.save(restaurantTable);
        RestaurantTableDTO result = restaurantTableMapper.toDto(restaurantTable);
        restaurantTableSearchRepository.save(restaurantTable);
        return result;
    }

    @Override
    public Optional<RestaurantTableDTO> partialUpdate(RestaurantTableDTO restaurantTableDTO) {
        log.debug("Request to partially update RestaurantTable : {}", restaurantTableDTO);

        return restaurantTableRepository
            .findById(restaurantTableDTO.getId())
            .map(existingRestaurantTable -> {
                restaurantTableMapper.partialUpdate(existingRestaurantTable, restaurantTableDTO);

                return existingRestaurantTable;
            })
            .map(restaurantTableRepository::save)
            .map(savedRestaurantTable -> {
                restaurantTableSearchRepository.save(savedRestaurantTable);

                return savedRestaurantTable;
            })
            .map(restaurantTableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RestaurantTables");
        return restaurantTableRepository.findAll(pageable).map(restaurantTableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RestaurantTableDTO> findOne(Long id) {
        log.debug("Request to get RestaurantTable : {}", id);
        return restaurantTableRepository.findById(id).map(restaurantTableMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RestaurantTable : {}", id);
        restaurantTableRepository.deleteById(id);
        restaurantTableSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantTableDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RestaurantTables for query {}", query);
        return restaurantTableSearchRepository.search(query, pageable).map(restaurantTableMapper::toDto);
    }
}
