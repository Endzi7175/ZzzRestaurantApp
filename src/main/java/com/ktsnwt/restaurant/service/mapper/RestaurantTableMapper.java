package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.RestaurantTable;
import com.ktsnwt.restaurant.service.dto.RestaurantTableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RestaurantTable} and its DTO {@link RestaurantTableDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RestaurantTableMapper extends EntityMapper<RestaurantTableDTO, RestaurantTable> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantTableDTO toDtoId(RestaurantTable restaurantTable);
}
