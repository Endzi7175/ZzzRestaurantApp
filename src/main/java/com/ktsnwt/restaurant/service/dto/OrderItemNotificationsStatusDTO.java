package com.ktsnwt.restaurant.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ktsnwt.restaurant.domain.OrderItemNotificationsStatus} entity.
 */
@Schema(description = "The OrderItemNotificationsStatus entity,\nRepresents status history of order item based on notifications.")
public class OrderItemNotificationsStatusDTO implements Serializable {

    private Long id;

    private Instant created;

    private Instant accepted;

    private Instant prepared;

    private Instant served;

    private Instant canceled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getAccepted() {
        return accepted;
    }

    public void setAccepted(Instant accepted) {
        this.accepted = accepted;
    }

    public Instant getPrepared() {
        return prepared;
    }

    public void setPrepared(Instant prepared) {
        this.prepared = prepared;
    }

    public Instant getServed() {
        return served;
    }

    public void setServed(Instant served) {
        this.served = served;
    }

    public Instant getCanceled() {
        return canceled;
    }

    public void setCanceled(Instant canceled) {
        this.canceled = canceled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemNotificationsStatusDTO)) {
            return false;
        }

        OrderItemNotificationsStatusDTO orderItemNotificationsStatusDTO = (OrderItemNotificationsStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemNotificationsStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemNotificationsStatusDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", accepted='" + getAccepted() + "'" +
            ", prepared='" + getPrepared() + "'" +
            ", served='" + getServed() + "'" +
            ", canceled='" + getCanceled() + "'" +
            "}";
    }
}
