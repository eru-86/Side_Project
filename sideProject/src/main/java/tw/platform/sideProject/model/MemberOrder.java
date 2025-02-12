package tw.platform.sideProject.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "memberorder")
public class MemberOrder {

    @EmbeddedId
    private MemberOrderKey id;

    @ManyToOne
    @MapsId("memberid")
    @JoinColumn(name = "memberid", insertable = false, updatable = false)
    private Member member;

    @ManyToOne
    @MapsId("orderid")
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private Order order;

    private Boolean owned;
    private Boolean wanted;
    private boolean collected;
    private String evaluate;
    private boolean getproject;
    private String picurl;   // 確保有這個屬性
    private String name;
    private Integer rank;


    // Getters and Setters
    
    
    public MemberOrderKey getId() {
        return id;
    }

    public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public void setId(MemberOrderKey id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
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

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	public boolean isGetproject() {
		return getproject;
	}

	public void setGetproject(boolean getproject) {
		this.getproject = getproject;
	}
	
	@Override
	public String toString() {
	    return "MemberOrder{" +
	            "orderid=" + getOrder() +
	            ", name='" + name + '\'' +
	            ", picurl='" + picurl + '\'' +
	            ", collected=" + collected +
	            '}';
	}

	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
}
