package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.RestaurantOrderItem;
import com.ktsnwt.restaurant.service.dto.RestaurantOrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RestaurantOrderItem} and its DTO {@link RestaurantOrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderItemNotificationsStatusMapper.class, RestaurantOrderMapper.class })
public interface RestaurantOrderItemMapper extends EntityMapper<RestaurantOrderItemDTO, RestaurantOrderItem> {
    @Mapping(target = "orderItemStatus", source = "orderItemStatus", qualifiedByName = "id")
    @Mapping(target = "restaurantOrder", source = "restaurantOrder", qualifiedByName = "id")
    RestaurantOrderItemDTO toDto(RestaurantOrderItem s);
}
