package tw.platform.sideProject.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "memberorder")
public class yuMemberOrder {

	@EmbeddedId
	private yuMemberOrderKey id;

	@ManyToOne
	@MapsId("memberid")
	@JoinColumn(name = "memberid", insertable = false, updatable = false)
	private yuMember member;

	@ManyToOne
	@MapsId("orderid")
	@JoinColumn(name = "orderid", insertable = false, updatable = false)
	private yuOrder order;

	private Boolean owned;

	private Boolean wanted;

	private Boolean collected;

	public yuMemberOrderKey getId() {
		return id;
	}

	public void setId(yuMemberOrderKey id) {
		this.id = id;
	}

	public yuMember getMember() {
		return member;
	}

	public void setMember(yuMember member) {
		this.member = member;
	}

	public yuOrder getOrder() {
		return order;
	}

	public void setOrder(yuOrder order) {
		this.order = order;
	}

	public Boolean getOwned() {
		return owned;
	}

	public void setOwned(Boolean owned) {
		this.owned = owned;
	}

	public Boolean getWanted() {
		return wanted;
	}

	public void setWanted(Boolean wanted) {
		this.wanted = wanted;
	}

	public Boolean getCollected() {
		return collected;
	}

	public void setCollected(Boolean collected) {
		this.collected = collected;
	}

}
