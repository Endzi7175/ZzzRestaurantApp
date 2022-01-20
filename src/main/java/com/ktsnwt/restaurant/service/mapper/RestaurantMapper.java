package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.Restaurant;
import com.ktsnwt.restaurant.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurant} and its DTO {@link RestaurantDTO}.
 */
@Mapper(componentModel = "spring", uses = { MenuMapper.class, UserMapper.class })
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {
    @Mapping(target = "menu", source = "menu", qualifiedByName = "id")
    @Mapping(target = "users", source = "users", qualifiedByName = "idSet")
    RestaurantDTO toDto(Restaurant s);

    @Mapping(target = "removeUser", ignore = true)
    Restaurant toEntity(RestaurantDTO restaurantDTO);
}
