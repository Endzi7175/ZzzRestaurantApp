package com.ktsnwt.restaurant.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.RestaurantTable} entity.
 */
public class RestaurantTableDTO implements Serializable {

    private Long id;

    private Integer tableNo;

    private Integer seatsNo;

    private String description;

    private Integer xPositionFrom;

    private Integer xPositionTo;

    private Integer yPositionFrom;

    private Integer yPositionTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public Integer getSeatsNo() {
        return seatsNo;
    }

    public void setSeatsNo(Integer seatsNo) {
        this.seatsNo = seatsNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getxPositionFrom() {
        return xPositionFrom;
    }

    public void setxPositionFrom(Integer xPositionFrom) {
        this.xPositionFrom = xPositionFrom;
    }

    public Integer getxPositionTo() {
        return xPositionTo;
    }

    public void setxPositionTo(Integer xPositionTo) {
        this.xPositionTo = xPositionTo;
    }

    public Integer getyPositionFrom() {
        return yPositionFrom;
    }

    public void setyPositionFrom(Integer yPositionFrom) {
        this.yPositionFrom = yPositionFrom;
    }

    public Integer getyPositionTo() {
        return yPositionTo;
    }

    public void setyPositionTo(Integer yPositionTo) {
        this.yPositionTo = yPositionTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantTableDTO)) {
            return false;
        }

        RestaurantTableDTO restaurantTableDTO = (RestaurantTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantTableDTO{" +
            "id=" + getId() +
            ", tableNo=" + getTableNo() +
            ", seatsNo=" + getSeatsNo() +
            ", description='" + getDescription() + "'" +
            ", xPositionFrom=" + getxPositionFrom() +
            ", xPositionTo=" + getxPositionTo() +
            ", yPositionFrom=" + getyPositionFrom() +
            ", yPositionTo=" + getyPositionTo() +
            "}";
    }
}
