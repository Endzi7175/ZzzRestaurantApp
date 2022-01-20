package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.Ingredient;
import com.ktsnwt.restaurant.repository.IngredientRepository;
import com.ktsnwt.restaurant.repository.search.IngredientSearchRepository;
import com.ktsnwt.restaurant.service.IngredientService;
import com.ktsnwt.restaurant.service.dto.IngredientDTO;
import com.ktsnwt.restaurant.service.mapper.IngredientMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ingredient}.
 */
@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final Logger log = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;

    private final IngredientSearchRepository ingredientSearchRepository;

    public IngredientServiceImpl(
        IngredientRepository ingredientRepository,
        IngredientMapper ingredientMapper,
        IngredientSearchRepository ingredientSearchRepository
    ) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.ingredientSearchRepository = ingredientSearchRepository;
    }

    @Override
    public IngredientDTO save(IngredientDTO ingredientDTO) {
        log.debug("Request to save Ingredient : {}", ingredientDTO);
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDTO);
        ingredient = ingredientRepository.save(ingredient);
        IngredientDTO result = ingredientMapper.toDto(ingredient);
        ingredientSearchRepository.save(ingredient);
        return result;
    }

    @Override
    public Optional<IngredientDTO> partialUpdate(IngredientDTO ingredientDTO) {
        log.debug("Request to partially update Ingredient : {}", ingredientDTO);

        return ingredientRepository
            .findById(ingredientDTO.getId())
            .map(existingIngredient -> {
                ingredientMapper.partialUpdate(existingIngredient, ingredientDTO);

                return existingIngredient;
            })
            .map(ingredientRepository::save)
            .map(savedIngredient -> {
                ingredientSearchRepository.save(savedIngredient);

                return savedIngredient;
            })
            .map(ingredientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ingredients");
        return ingredientRepository.findAll(pageable).map(ingredientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientDTO> findOne(Long id) {
        log.debug("Request to get Ingredient : {}", id);
        return ingredientRepository.findById(id).map(ingredientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingredient : {}", id);
        ingredientRepository.deleteById(id);
        ingredientSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredientDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ingredients for query {}", query);
        return ingredientSearchRepository.search(query, pageable).map(ingredientMapper::toDto);
    }
}
