package com.ktsnwt.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ktsnwt.restaurant.domain.enumeration.OrderItemStatus;
import java.io.Serializable;
import javax.persistence.*;

/**
 * The OrderItem entity,\nRepresents an item in the Order.
 */
@Entity
@Table(name = "restaurant_order_item")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "restaurantorderitem")
public class RestaurantOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "menu_item_id")
    private Long menuItemId;

    @Column(name = "price_excluding_tax")
    private Float priceExcludingTax;

    @Column(name = "price_including_tax")
    private Float priceIncludingTax;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderItemStatus status;

    @JsonIgnoreProperties(value = { "orderItem" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private OrderItemNotificationsStatus orderItemStatus;

    /**
     * order
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "items", "user", "restaurantTable" }, allowSetters = true)
    private RestaurantOrder restaurantOrder;


    public Long getId() {
        return this.id;
    }

    public RestaurantOrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RestaurantOrderItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMenuItemId() {
        return this.menuItemId;
    }

    public RestaurantOrderItem menuItemId(Long menuItemId) {
        this.setMenuItemId(menuItemId);
        return this;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Float getPriceExcludingTax() {
        return this.priceExcludingTax;
    }

    public RestaurantOrderItem priceExcludingTax(Float priceExcludingTax) {
        this.setPriceExcludingTax(priceExcludingTax);
        return this;
    }

    public void setPriceExcludingTax(Float priceExcludingTax) {
        this.priceExcludingTax = priceExcludingTax;
    }

    public Float getPriceIncludingTax() {
        return this.priceIncludingTax;
    }

    public RestaurantOrderItem priceIncludingTax(Float priceIncludingTax) {
        this.setPriceIncludingTax(priceIncludingTax);
        return this;
    }

    public void setPriceIncludingTax(Float priceIncludingTax) {
        this.priceIncludingTax = priceIncludingTax;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public RestaurantOrderItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItemStatus getStatus() {
        return this.status;
    }

    public RestaurantOrderItem status(OrderItemStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public OrderItemNotificationsStatus getOrderItemStatus() {
        return this.orderItemStatus;
    }

    public void setOrderItemStatus(OrderItemNotificationsStatus orderItemNotificationsStatus) {
        this.orderItemStatus = orderItemNotificationsStatus;
    }

    public RestaurantOrderItem orderItemStatus(OrderItemNotificationsStatus orderItemNotificationsStatus) {
        this.setOrderItemStatus(orderItemNotificationsStatus);
        return this;
    }

    public RestaurantOrder getRestaurantOrder() {
        return this.restaurantOrder;
    }

    public void setRestaurantOrder(RestaurantOrder restaurantOrder) {
        this.restaurantOrder = restaurantOrder;
    }

    public RestaurantOrderItem restaurantOrder(RestaurantOrder restaurantOrder) {
        this.setRestaurantOrder(restaurantOrder);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantOrderItem)) {
            return false;
        }
        return id != null && id.equals(((RestaurantOrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantOrderItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", menuItemId=" + getMenuItemId() +
            ", priceExcludingTax=" + getPriceExcludingTax() +
            ", priceIncludingTax=" + getPriceIncludingTax() +
            ", quantity=" + getQuantity() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
