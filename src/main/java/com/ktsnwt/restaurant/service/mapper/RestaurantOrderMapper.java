package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.RestaurantOrder;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RestaurantOrder} and its DTO {@link RestaurantOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, RestaurantTableMapper.class })
public interface RestaurantOrderMapper extends EntityMapper<RestaurantOrderDTO, RestaurantOrder> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "restaurantTable", source = "restaurantTable", qualifiedByName = "id")
    RestaurantOrderDTO toDto(RestaurantOrder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantOrderDTO toDtoId(RestaurantOrder restaurantOrder);
}
