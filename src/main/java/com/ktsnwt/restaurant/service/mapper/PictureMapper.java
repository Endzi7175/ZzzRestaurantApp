package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.Picture;
import com.ktsnwt.restaurant.service.dto.PictureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Picture} and its DTO {@link PictureDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PictureMapper extends EntityMapper<PictureDTO, Picture> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PictureDTO toDtoId(Picture picture);
}
