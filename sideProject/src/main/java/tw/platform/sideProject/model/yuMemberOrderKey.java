package tw.platform.sideProject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class yuMemberOrderKey implements Serializable {

	@Column(name = "memberid")
    private Long memberid;
	@Column(name = "orderid")
    private Long orderid;

    // Constructors
    public yuMemberOrderKey() {}

    public yuMemberOrderKey(Long memberid, Long orderid) {
        this.memberid = memberid;
        this.orderid = orderid;
    }

    // Getters and Setters
    public Long getMemberid() {
        return memberid;
    }

    public void setMemberid(Long memberid) {
        this.memberid = memberid;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    // Override hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(memberid, orderid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        yuMemberOrderKey that = (yuMemberOrderKey) obj;
        return Objects.equals(memberid, that.memberid) && Objects.equals(orderid, that.orderid);
    }
}
