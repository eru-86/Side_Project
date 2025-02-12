package tw.platform.sideProject.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderTagKey implements Serializable {

    private Long orderId;
    private int tagId;

    // Constructors
    public OrderTagKey() {}

    public OrderTagKey(Long orderId, int tagId) {
        this.orderId = orderId;
        this.tagId = tagId;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    // Override hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(orderId, tagId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderTagKey that = (OrderTagKey) obj;
        return Objects.equals(orderId, that.orderId) && Objects.equals(tagId, that.tagId);
    }
}
