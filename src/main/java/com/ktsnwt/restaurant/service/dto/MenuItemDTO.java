package com.ktsnwt.restaurant.service.dto;

import com.ktsnwt.restaurant.domain.enumeration.MenuItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.MenuItem} entity.
 */
@Schema(description = "The MenuItem entity,\nRepresents an item in the menu.")
public class MenuItemDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    /**
     * price of the meal/dish
     */
    @Schema(description = "price of the meal/dish")
    private Float price;

    /**
     * time needed to prepare meal in minutes
     */
    @Schema(description = "time needed to prepare meal in minutes")
    private Integer prepareTime;

    private MenuItemType type;

    private PictureDTO picture;

    private Set<IngredientDTO> ingredients = new HashSet<>();

    private MenuDTO menu;

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(Integer prepareTime) {
        this.prepareTime = prepareTime;
    }

    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public PictureDTO getPicture() {
        return picture;
    }

    public void setPicture(PictureDTO picture) {
        this.picture = picture;
    }

    public Set<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItemDTO)) {
            return false;
        }

        MenuItemDTO menuItemDTO = (MenuItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, menuItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", prepareTime=" + getPrepareTime() +
            ", type='" + getType() + "'" +
            ", picture=" + getPicture() +
            ", ingredients=" + getIngredients() +
            ", menu=" + getMenu() +
            "}";
    }
}
