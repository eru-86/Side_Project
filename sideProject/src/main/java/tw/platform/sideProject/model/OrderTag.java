package tw.platform.sideProject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordertag")
public class OrderTag {

    @EmbeddedId
    private OrderTagKey id;

    @ManyToOne
    @MapsId("orderId")
	@JsonBackReference
    @JoinColumn(name = "orderid")
    private Order order;

    @ManyToOne
    @MapsId("tagId")
	@JsonBackReference
    @JoinColumn(name = "tagid")
    private Tag tag;

    // Getters and Setters
    public OrderTagKey getId() {
        return id;
    }

    public void setId(OrderTagKey id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
