package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.MenuItem;
import com.ktsnwt.restaurant.repository.MenuItemRepository;
import com.ktsnwt.restaurant.repository.search.MenuItemSearchRepository;
import com.ktsnwt.restaurant.service.MenuItemService;
import com.ktsnwt.restaurant.service.dto.MenuItemDTO;
import com.ktsnwt.restaurant.service.mapper.MenuItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MenuItem}.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    private final MenuItemSearchRepository menuItemSearchRepository;

    public MenuItemServiceImpl(
        MenuItemRepository menuItemRepository,
        MenuItemMapper menuItemMapper,
        MenuItemSearchRepository menuItemSearchRepository
    ) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
        this.menuItemSearchRepository = menuItemSearchRepository;
    }

    @Override
    public MenuItemDTO save(MenuItemDTO menuItemDTO) {
        log.debug("Request to save MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        MenuItemDTO result = menuItemMapper.toDto(menuItem);
        //        menuItemSearchRepository.save(menuItem);
        return result;
    }

    @Override
    public Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO) {
        log.debug("Request to partially update MenuItem : {}", menuItemDTO);

        return menuItemRepository
            .findById(menuItemDTO.getId())
            .map(existingMenuItem -> {
                menuItemMapper.partialUpdate(existingMenuItem, menuItemDTO);

                return existingMenuItem;
            })
            .map(menuItemRepository::save)
            .map(savedMenuItem -> {
                menuItemSearchRepository.save(savedMenuItem);

                return savedMenuItem;
            })
            .map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MenuItems");
        return menuItemRepository.findAll(pageable).map(menuItemMapper::toDto);
    }

    public Page<MenuItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return menuItemRepository.findAllWithEagerRelationships(pageable).map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> findOne(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findOneWithEagerRelationships(id).map(menuItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
        menuItemSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MenuItems for query {}", query);
        return menuItemSearchRepository.search(query, pageable).map(menuItemMapper::toDto);
    }
}
