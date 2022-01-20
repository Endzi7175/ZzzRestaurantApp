package com.ktsnwt.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * The OrderItemNotificationsStatus entity,\nRepresents status history of order item based on notifications.
 */
@Entity
@Table(name = "order_item_notification")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "orderitemnotificationsstatus")
public class OrderItemNotificationsStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created")
    private Instant created;

    @Column(name = "accepted")
    private Instant accepted;

    @Column(name = "prepared")
    private Instant prepared;

    @Column(name = "served")
    private Instant served;

    @Column(name = "canceled")
    private Instant canceled;

    @JsonIgnoreProperties(value = { "orderItemStatus", "restaurantOrder" }, allowSetters = true)
    @OneToOne(mappedBy = "orderItemStatus")
    private RestaurantOrderItem orderItem;


    public Long getId() {
        return this.id;
    }

    public OrderItemNotificationsStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return this.created;
    }

    public OrderItemNotificationsStatus created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getAccepted() {
        return this.accepted;
    }

    public OrderItemNotificationsStatus accepted(Instant accepted) {
        this.setAccepted(accepted);
        return this;
    }

    public void setAccepted(Instant accepted) {
        this.accepted = accepted;
    }

    public Instant getPrepared() {
        return this.prepared;
    }

    public OrderItemNotificationsStatus prepared(Instant prepared) {
        this.setPrepared(prepared);
        return this;
    }

    public void setPrepared(Instant prepared) {
        this.prepared = prepared;
    }

    public Instant getServed() {
        return this.served;
    }

    public OrderItemNotificationsStatus served(Instant served) {
        this.setServed(served);
        return this;
    }

    public void setServed(Instant served) {
        this.served = served;
    }

    public Instant getCanceled() {
        return this.canceled;
    }

    public OrderItemNotificationsStatus canceled(Instant canceled) {
        this.setCanceled(canceled);
        return this;
    }

    public void setCanceled(Instant canceled) {
        this.canceled = canceled;
    }

    public RestaurantOrderItem getOrderItem() {
        return this.orderItem;
    }

    public void setOrderItem(RestaurantOrderItem restaurantOrderItem) {
        if (this.orderItem != null) {
            this.orderItem.setOrderItemStatus(null);
        }
        if (restaurantOrderItem != null) {
            restaurantOrderItem.setOrderItemStatus(this);
        }
        this.orderItem = restaurantOrderItem;
    }

    public OrderItemNotificationsStatus orderItem(RestaurantOrderItem restaurantOrderItem) {
        this.setOrderItem(restaurantOrderItem);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemNotificationsStatus)) {
            return false;
        }
        return id != null && id.equals(((OrderItemNotificationsStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemNotificationsStatus{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", accepted='" + getAccepted() + "'" +
            ", prepared='" + getPrepared() + "'" +
            ", served='" + getServed() + "'" +
            ", canceled='" + getCanceled() + "'" +
            "}";
    }
}
