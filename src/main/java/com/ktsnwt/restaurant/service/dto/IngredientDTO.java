package com.ktsnwt.restaurant.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.Ingredient} entity.
 */
@Schema(description = "The Ingredient entity,\nRepresents an ingredient of the meal from the menu.")
public class IngredientDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean alergen;

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

    public Boolean getAlergen() {
        return alergen;
    }

    public void setAlergen(Boolean alergen) {
        this.alergen = alergen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientDTO)) {
            return false;
        }

        IngredientDTO ingredientDTO = (IngredientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingredientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredientDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alergen='" + getAlergen() + "'" +
            "}";
    }
}
