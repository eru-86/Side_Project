package tw.platform.sideProject.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {

	@Id
	private int tagid;
//    private int tagidm;
	private String tagname;

	// 關聯到 MemberTag
	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MemberTag> memberTags = new HashSet<>();

	// 關聯到 OrderTag
	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderTag> orderTags = new HashSet<>();

	// Constructors
	public Tag() {
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public Set<MemberTag> getMemberTags() {
		return memberTags;
	}

	public void setMemberTags(Set<MemberTag> memberTags) {
		this.memberTags = memberTags;
	}

	public Set<OrderTag> getOrderTags() {
		return orderTags;
	}

	public void setOrderTags(Set<OrderTag> orderTags) {
		this.orderTags = orderTags;
	}

	public int getTagid() {
		return tagid;
	}

	public void setTagid(int tagid) {
		this.tagid = tagid;
	}
}
