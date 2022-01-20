package com.ktsnwt.restaurant.service.dto;

import com.ktsnwt.restaurant.domain.enumeration.OrderItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.RestaurantOrderItem} entity.
 */
@Schema(description = "The OrderItem entity,\nRepresents an item in the Order.")
public class RestaurantOrderItemDTO implements Serializable {

    private Long id;

    private String name;

    private Long menuItemId;

    private Float priceExcludingTax;

    private Float priceIncludingTax;

    private Integer quantity;

    private OrderItemStatus status;

    private OrderItemNotificationsStatusDTO orderItemStatus;

    private RestaurantOrderDTO restaurantOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Float getPriceExcludingTax() {
        return priceExcludingTax;
    }

    public void setPriceExcludingTax(Float priceExcludingTax) {
        this.priceExcludingTax = priceExcludingTax;
    }

    public Float getPriceIncludingTax() {
        return priceIncludingTax;
    }

    public void setPriceIncludingTax(Float priceIncludingTax) {
        this.priceIncludingTax = priceIncludingTax;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public OrderItemNotificationsStatusDTO getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(OrderItemNotificationsStatusDTO orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }

    public RestaurantOrderDTO getRestaurantOrder() {
        return restaurantOrder;
    }

    public void setRestaurantOrder(RestaurantOrderDTO restaurantOrder) {
        this.restaurantOrder = restaurantOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantOrderItemDTO)) {
            return false;
        }

        RestaurantOrderItemDTO restaurantOrderItemDTO = (RestaurantOrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantOrderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantOrderItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", menuItemId=" + getMenuItemId() +
            ", priceExcludingTax=" + getPriceExcludingTax() +
            ", priceIncludingTax=" + getPriceIncludingTax() +
            ", quantity=" + getQuantity() +
            ", status='" + getStatus() + "'" +
            ", orderItemStatus=" + getOrderItemStatus() +
            ", restaurantOrder=" + getRestaurantOrder() +
            "}";
    }
}
