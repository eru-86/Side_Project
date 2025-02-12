package tw.platform.sideProject.model;

public class AddMemberOrderRequest {
    private Long memberId;
    private Long orderId;
    private boolean owned;
    private boolean wanted;
    private boolean collected;
    private String evaluate;
    private boolean getproject;
    private Integer rank;

    // Getters and Setters
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public boolean isWanted() {
        return wanted;
    }

    public void setWanted(boolean wanted) {
        this.wanted = wanted;
    }

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
    
	public boolean isGetproject() {
		return getproject;
	}

	public void setGetproject(boolean getproject) {
		this.getproject = getproject;
	}

    public Integer getRank() {
		return rank;
	}
    
	public void setRank(Integer rank) {
		this.rank = rank;
	}
}

