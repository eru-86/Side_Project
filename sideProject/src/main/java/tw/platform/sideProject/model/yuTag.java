package tw.platform.sideProject.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class yuTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagid;

	private String tagname;

	@ManyToMany(mappedBy = "tags")
	private Set<yuOrder> orders = new HashSet<>();
	
	@ManyToMany(mappedBy = "tags")
	private Set<yuMember> members = new HashSet<>();


	// Getters and Setters
	public Long getTagid() {
		return tagid;
	}

	public void setTagid(Long tagid) {
		this.tagid = tagid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public Set<yuOrder> getOrders() {
		return orders;
	}

	public void setOrders(Set<yuOrder> orders) {
		this.orders = orders;
	}

	public Set<yuMember> getMembers() {
		return members;
	}

	public void setMembers(Set<yuMember> members) {
		this.members = members;
	}

}
