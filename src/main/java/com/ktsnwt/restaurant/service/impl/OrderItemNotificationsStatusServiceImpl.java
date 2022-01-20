package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.OrderItemNotificationsStatus;
import com.ktsnwt.restaurant.repository.OrderItemNotificationsStatusRepository;
import com.ktsnwt.restaurant.repository.search.OrderItemNotificationsStatusSearchRepository;
import com.ktsnwt.restaurant.service.OrderItemNotificationsStatusService;
import com.ktsnwt.restaurant.service.dto.OrderItemNotificationsStatusDTO;
import com.ktsnwt.restaurant.service.mapper.OrderItemNotificationsStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderItemNotificationsStatus}.
 */
@Service
@Transactional
public class OrderItemNotificationsStatusServiceImpl implements OrderItemNotificationsStatusService {

    private final Logger log = LoggerFactory.getLogger(OrderItemNotificationsStatusServiceImpl.class);

    private final OrderItemNotificationsStatusRepository orderItemNotificationsStatusRepository;

    private final OrderItemNotificationsStatusMapper orderItemNotificationsStatusMapper;

    private final OrderItemNotificationsStatusSearchRepository orderItemNotificationsStatusSearchRepository;

    public OrderItemNotificationsStatusServiceImpl(
        OrderItemNotificationsStatusRepository orderItemNotificationsStatusRepository,
        OrderItemNotificationsStatusMapper orderItemNotificationsStatusMapper,
        OrderItemNotificationsStatusSearchRepository orderItemNotificationsStatusSearchRepository
    ) {
        this.orderItemNotificationsStatusRepository = orderItemNotificationsStatusRepository;
        this.orderItemNotificationsStatusMapper = orderItemNotificationsStatusMapper;
        this.orderItemNotificationsStatusSearchRepository = orderItemNotificationsStatusSearchRepository;
    }

    @Override
    public OrderItemNotificationsStatusDTO save(OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO) {
        log.debug("Request to save OrderItemNotificationsStatus : {}", orderItemNotificationsStatusDTO);
        OrderItemNotificationsStatus orderItemNotificationsStatus = orderItemNotificationsStatusMapper.toEntity(
            orderItemNotificationsStatusDTO
        );
        orderItemNotificationsStatus = orderItemNotificationsStatusRepository.save(orderItemNotificationsStatus);
        OrderItemNotificationsStatusDTO result = orderItemNotificationsStatusMapper.toDto(orderItemNotificationsStatus);
        orderItemNotificationsStatusSearchRepository.save(orderItemNotificationsStatus);
        return result;
    }

    @Override
    public Optional<OrderItemNotificationsStatusDTO> partialUpdate(OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO) {
        log.debug("Request to partially update OrderItemNotificationsStatus : {}", orderItemNotificationsStatusDTO);

        return orderItemNotificationsStatusRepository
            .findById(orderItemNotificationsStatusDTO.getId())
            .map(existingOrderItemNotificationsStatus -> {
                orderItemNotificationsStatusMapper.partialUpdate(existingOrderItemNotificationsStatus, orderItemNotificationsStatusDTO);

                return existingOrderItemNotificationsStatus;
            })
            .map(orderItemNotificationsStatusRepository::save)
            .map(savedOrderItemNotificationsStatus -> {
                orderItemNotificationsStatusSearchRepository.save(savedOrderItemNotificationsStatus);

                return savedOrderItemNotificationsStatus;
            })
            .map(orderItemNotificationsStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemNotificationsStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItemNotificationsStatuses");
        return orderItemNotificationsStatusRepository.findAll(pageable).map(orderItemNotificationsStatusMapper::toDto);
    }

    /**
     *  Get all the orderItemNotificationsStatuses where OrderItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderItemNotificationsStatusDTO> findAllWhereOrderItemIsNull() {
        log.debug("Request to get all orderItemNotificationsStatuses where OrderItem is null");
        return StreamSupport
            .stream(orderItemNotificationsStatusRepository.findAll().spliterator(), false)
            .filter(orderItemNotificationsStatus -> orderItemNotificationsStatus.getOrderItem() == null)
            .map(orderItemNotificationsStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderItemNotificationsStatusDTO> findOne(Long id) {
        log.debug("Request to get OrderItemNotificationsStatus : {}", id);
        return orderItemNotificationsStatusRepository.findById(id).map(orderItemNotificationsStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderItemNotificationsStatus : {}", id);
        orderItemNotificationsStatusRepository.deleteById(id);
        orderItemNotificationsStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemNotificationsStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderItemNotificationsStatuses for query {}", query);
        return orderItemNotificationsStatusSearchRepository.search(query, pageable).map(orderItemNotificationsStatusMapper::toDto);
    }
}
