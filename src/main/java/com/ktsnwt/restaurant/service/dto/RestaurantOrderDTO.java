package com.ktsnwt.restaurant.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.RestaurantOrder} entity.
 */
@Schema(description = "The Order entity,\nRepresents a single order for a table.")
public class RestaurantOrderDTO implements Serializable {

    private Long id;

    private ZonedDateTime date;

    private Float priceExcludingTax;

    private Float priceIncludingTax;

    private UserDTO user;

    private RestaurantTableDTO restaurantTable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RestaurantTableDTO getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTableDTO restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantOrderDTO)) {
            return false;
        }

        RestaurantOrderDTO restaurantOrderDTO = (RestaurantOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantOrderDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", priceExcludingTax=" + getPriceExcludingTax() +
            ", priceIncludingTax=" + getPriceIncludingTax() +
            ", user=" + getUser() +
            ", restaurantTable=" + getRestaurantTable() +
            "}";
    }
}
