package com.ktsnwt.restaurant.service.mapper;

import com.ktsnwt.restaurant.domain.OrderItemNotificationsStatus;
import com.ktsnwt.restaurant.service.dto.OrderItemNotificationsStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItemNotificationsStatus} and its DTO {@link OrderItemNotificationsStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderItemNotificationsStatusMapper extends EntityMapper<OrderItemNotificationsStatusDTO, OrderItemNotificationsStatus> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemNotificationsStatusDTO toDtoId(OrderItemNotificationsStatus orderItemNotificationsStatus);
}
