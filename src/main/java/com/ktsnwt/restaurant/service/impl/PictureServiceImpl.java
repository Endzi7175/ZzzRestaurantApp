package com.ktsnwt.restaurant.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.ktsnwt.restaurant.domain.Picture;
import com.ktsnwt.restaurant.repository.PictureRepository;
import com.ktsnwt.restaurant.repository.search.PictureSearchRepository;
import com.ktsnwt.restaurant.service.PictureService;
import com.ktsnwt.restaurant.service.dto.PictureDTO;
import com.ktsnwt.restaurant.service.mapper.PictureMapper;
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
 * Service Implementation for managing {@link Picture}.
 */
@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    private final PictureRepository pictureRepository;

    private final PictureMapper pictureMapper;

    private final PictureSearchRepository pictureSearchRepository;

    public PictureServiceImpl(
        PictureRepository pictureRepository,
        PictureMapper pictureMapper,
        PictureSearchRepository pictureSearchRepository
    ) {
        this.pictureRepository = pictureRepository;
        this.pictureMapper = pictureMapper;
        this.pictureSearchRepository = pictureSearchRepository;
    }

    @Override
    public PictureDTO save(PictureDTO pictureDTO) {
        log.debug("Request to save Picture : {}", pictureDTO);
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        PictureDTO result = pictureMapper.toDto(picture);
        pictureSearchRepository.save(picture);
        return result;
    }

    @Override
    public Optional<PictureDTO> partialUpdate(PictureDTO pictureDTO) {
        log.debug("Request to partially update Picture : {}", pictureDTO);

        return pictureRepository
            .findById(pictureDTO.getId())
            .map(existingPicture -> {
                pictureMapper.partialUpdate(existingPicture, pictureDTO);

                return existingPicture;
            })
            .map(pictureRepository::save)
            .map(savedPicture -> {
                pictureSearchRepository.save(savedPicture);

                return savedPicture;
            })
            .map(pictureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PictureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pictures");
        return pictureRepository.findAll(pageable).map(pictureMapper::toDto);
    }

    /**
     *  Get all the pictures where MenuItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PictureDTO> findAllWhereMenuItemIsNull() {
        log.debug("Request to get all pictures where MenuItem is null");
        return StreamSupport
            .stream(pictureRepository.findAll().spliterator(), false)
            .filter(picture -> picture.getMenuItem() == null)
            .map(pictureMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PictureDTO> findOne(Long id) {
        log.debug("Request to get Picture : {}", id);
        return pictureRepository.findById(id).map(pictureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Picture : {}", id);
        pictureRepository.deleteById(id);
        pictureSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PictureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Pictures for query {}", query);
        return pictureSearchRepository.search(query, pageable).map(pictureMapper::toDto);
    }
}
