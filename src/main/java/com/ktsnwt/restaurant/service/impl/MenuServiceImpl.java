package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.Menu;
import com.ktsnwt.restaurant.repository.MenuRepository;
import com.ktsnwt.restaurant.repository.search.MenuSearchRepository;
import com.ktsnwt.restaurant.service.MenuService;
import com.ktsnwt.restaurant.service.dto.MenuDTO;
import com.ktsnwt.restaurant.service.mapper.MenuMapper;
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
 * Service Implementation for managing {@link Menu}.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    private final MenuSearchRepository menuSearchRepository;

    public MenuServiceImpl(MenuRepository menuRepository, MenuMapper menuMapper, MenuSearchRepository menuSearchRepository) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
        this.menuSearchRepository = menuSearchRepository;
    }

    @Override
    public MenuDTO save(MenuDTO menuDTO) {
        log.debug("Request to save Menu : {}", menuDTO);
        Menu menu = menuMapper.toEntity(menuDTO);
        menu = menuRepository.save(menu);
        MenuDTO result = menuMapper.toDto(menu);
        menuSearchRepository.save(menu);
        return result;
    }

    @Override
    public Optional<MenuDTO> partialUpdate(MenuDTO menuDTO) {
        log.debug("Request to partially update Menu : {}", menuDTO);

        return menuRepository
            .findById(menuDTO.getId())
            .map(existingMenu -> {
                menuMapper.partialUpdate(existingMenu, menuDTO);

                return existingMenu;
            })
            .map(menuRepository::save)
            .map(savedMenu -> {
                menuSearchRepository.save(savedMenu);

                return savedMenu;
            })
            .map(menuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Menus");
        return menuRepository.findAll(pageable).map(menuMapper::toDto);
    }

    /**
     *  Get all the menus where Restaurant is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MenuDTO> findAllWhereRestaurantIsNull() {
        log.debug("Request to get all menus where Restaurant is null");
        return StreamSupport
            .stream(menuRepository.findAll().spliterator(), false)
            .filter(menu -> menu.getRestaurant() == null)
            .map(menuMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuDTO> findOne(Long id) {
        log.debug("Request to get Menu : {}", id);
        return menuRepository.findById(id).map(menuMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Menu : {}", id);
        menuRepository.deleteById(id);
        menuSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Menus for query {}", query);
        return menuSearchRepository.search(query, pageable).map(menuMapper::toDto);
    }
}
