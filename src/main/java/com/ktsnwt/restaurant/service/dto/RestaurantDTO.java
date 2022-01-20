package com.ktsnwt.restaurant.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.Restaurant} entity.
 */
@Schema(description = "The Restaurant entity,\nRepresents the restaurant.")
public class RestaurantDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String address;

    private String phone;

    private Integer xTablePositions;

    private Integer yTablePositions;

    private MenuDTO menu;

    private Set<UserDTO> users = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getxTablePositions() {
        return xTablePositions;
    }

    public void setxTablePositions(Integer xTablePositions) {
        this.xTablePositions = xTablePositions;
    }

    public Integer getyTablePositions() {
        return yTablePositions;
    }

    public void setyTablePositions(Integer yTablePositions) {
        this.yTablePositions = yTablePositions;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantDTO)) {
            return false;
        }

        RestaurantDTO restaurantDTO = (RestaurantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", xTablePositions=" + getxTablePositions() +
            ", yTablePositions=" + getyTablePositions() +
            ", menu=" + getMenu() +
            ", users=" + getUsers() +
            "}";
    }
}
