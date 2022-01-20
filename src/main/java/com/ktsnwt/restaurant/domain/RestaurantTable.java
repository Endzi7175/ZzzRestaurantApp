package com.ktsnwt.restaurant.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A RestaurantTable.
 */
@Entity
@Table(name = "restaurant_table")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "restauranttable")
public class RestaurantTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_no")
    private Integer tableNo;

    @Column(name = "seats_no")
    private Integer seatsNo;

    @Column(name = "description")
    private String description;

    @Column(name = "x_position_from")
    private Integer xPositionFrom;

    @Column(name = "x_position_to")
    private Integer xPositionTo;

    @Column(name = "y_position_from")
    private Integer yPositionFrom;

    @Column(name = "y_position_to")
    private Integer yPositionTo;


    public Long getId() {
        return this.id;
    }

    public RestaurantTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTableNo() {
        return this.tableNo;
    }

    public RestaurantTable tableNo(Integer tableNo) {
        this.setTableNo(tableNo);
        return this;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public Integer getSeatsNo() {
        return this.seatsNo;
    }

    public RestaurantTable seatsNo(Integer seatsNo) {
        this.setSeatsNo(seatsNo);
        return this;
    }

    public void setSeatsNo(Integer seatsNo) {
        this.seatsNo = seatsNo;
    }

    public String getDescription() {
        return this.description;
    }

    public RestaurantTable description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getxPositionFrom() {
        return this.xPositionFrom;
    }

    public RestaurantTable xPositionFrom(Integer xPositionFrom) {
        this.setxPositionFrom(xPositionFrom);
        return this;
    }

    public void setxPositionFrom(Integer xPositionFrom) {
        this.xPositionFrom = xPositionFrom;
    }

    public Integer getxPositionTo() {
        return this.xPositionTo;
    }

    public RestaurantTable xPositionTo(Integer xPositionTo) {
        this.setxPositionTo(xPositionTo);
        return this;
    }

    public void setxPositionTo(Integer xPositionTo) {
        this.xPositionTo = xPositionTo;
    }

    public Integer getyPositionFrom() {
        return this.yPositionFrom;
    }

    public RestaurantTable yPositionFrom(Integer yPositionFrom) {
        this.setyPositionFrom(yPositionFrom);
        return this;
    }

    public void setyPositionFrom(Integer yPositionFrom) {
        this.yPositionFrom = yPositionFrom;
    }

    public Integer getyPositionTo() {
        return this.yPositionTo;
    }

    public RestaurantTable yPositionTo(Integer yPositionTo) {
        this.setyPositionTo(yPositionTo);
        return this;
    }

    public void setyPositionTo(Integer yPositionTo) {
        this.yPositionTo = yPositionTo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantTable)) {
            return false;
        }
        return id != null && id.equals(((RestaurantTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantTable{" +
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
