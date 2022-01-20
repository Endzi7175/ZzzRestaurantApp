package com.ktsnwt.restaurant.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.Picture} entity.
 */
public class PictureDTO implements Serializable {

    private Long id;

    private String name;

    private String pictureUrl;

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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PictureDTO)) {
            return false;
        }

        PictureDTO pictureDTO = (PictureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pictureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PictureDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pictureUrl='" + getPictureUrl() + "'" +
            "}";
    }
}
