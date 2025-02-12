package tw.platform.sideProject.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class mimiTag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagid;
	// private Long tagidm;
	private String tagname;
	private String category;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getTagid() {
		return tagid;
	}
	public void setTagid(Long tagid) {
		this.tagid = tagid;
	}
	// public Long getTagidm() {
	// 	return tagidm;
	// }
	// public void setTagidm(Long tagidm) {
	// 	this.tagidm = tagidm;
	// }
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	
//--------------------------------------------
	@ManyToMany(mappedBy = "tags")
	private List<mimiMember> member;


	public List<mimiMember> getMember() {
		return member;
	}
	public void setMember(List<mimiMember> member) {
		this.member = member;
	}
	
	
}
