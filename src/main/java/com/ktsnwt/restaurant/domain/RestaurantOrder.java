package com.ktsnwt.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * The Order entity,\nRepresents a single order for a table.
 */
@Entity
@Table(name = "restaurant_order")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "restaurantorder")
public class RestaurantOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "price_excluding_tax")
    private Float priceExcludingTax;

    @Column(name = "price_including_tax")
    private Float priceIncludingTax;

    /**
     * order items
     */
    @OneToMany(mappedBy = "restaurantOrder")
    @JsonIgnoreProperties(value = { "orderItemStatus", "restaurantOrder" }, allowSetters = true)
    private Set<RestaurantOrderItem> items = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private RestaurantTable restaurantTable;


    public Long getId() {
        return this.id;
    }

    public RestaurantOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public RestaurantOrder date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Float getPriceExcludingTax() {
        return this.priceExcludingTax;
    }

    public RestaurantOrder priceExcludingTax(Float priceExcludingTax) {
        this.setPriceExcludingTax(priceExcludingTax);
        return this;
    }

    public void setPriceExcludingTax(Float priceExcludingTax) {
        this.priceExcludingTax = priceExcludingTax;
    }

    public Float getPriceIncludingTax() {
        return this.priceIncludingTax;
    }

    public RestaurantOrder priceIncludingTax(Float priceIncludingTax) {
        this.setPriceIncludingTax(priceIncludingTax);
        return this;
    }

    public void setPriceIncludingTax(Float priceIncludingTax) {
        this.priceIncludingTax = priceIncludingTax;
    }

    public Set<RestaurantOrderItem> getItems() {
        return this.items;
    }

    public void setItems(Set<RestaurantOrderItem> restaurantOrderItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setRestaurantOrder(null));
        }
        if (restaurantOrderItems != null) {
            restaurantOrderItems.forEach(i -> i.setRestaurantOrder(this));
        }
        this.items = restaurantOrderItems;
    }

    public RestaurantOrder items(Set<RestaurantOrderItem> restaurantOrderItems) {
        this.setItems(restaurantOrderItems);
        return this;
    }

    public RestaurantOrder addItem(RestaurantOrderItem restaurantOrderItem) {
        this.items.add(restaurantOrderItem);
        restaurantOrderItem.setRestaurantOrder(this);
        return this;
    }

    public RestaurantOrder removeItem(RestaurantOrderItem restaurantOrderItem) {
        this.items.remove(restaurantOrderItem);
        restaurantOrderItem.setRestaurantOrder(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RestaurantOrder user(User user) {
        this.setUser(user);
        return this;
    }

    public RestaurantTable getRestaurantTable() {
        return this.restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public RestaurantOrder restaurantTable(RestaurantTable restaurantTable) {
        this.setRestaurantTable(restaurantTable);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantOrder)) {
            return false;
        }
        return id != null && id.equals(((RestaurantOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantOrder{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", priceExcludingTax=" + getPriceExcludingTax() +
            ", priceIncludingTax=" + getPriceIncludingTax() +
            "}";
    }
}
