package tw.platform.sideProject.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderform")
public class yuOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderid;

	private String name;
	private Date deadline; //Deadline的類型改為Date 
	private String intro;
	private String detail;
	private String picurl;
	private String location;
	private String rank;
	private String category;
	private Integer people = 1;
	private Long collectCount;

	@ManyToMany
	@JoinTable(name = "ordertag", joinColumns = @JoinColumn(name = "orderid"), inverseJoinColumns = @JoinColumn(name = "tagid"))
	private Set<yuTag> tags = new HashSet<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MemberOrder> memberOrders = new ArrayList<>();

	// get set
	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//Deadline的類型改為Date 
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Integer getPeople() {
		return people;
	}

	public void setPeople(Integer people) {
		this.people = people;
	}

	// 改寫getTagNames
	public String getTagNames() {
		return tags.stream().map(tag -> {
			String cssClass = getTagCssClass(tag.getTagname());
			return "<span class=\"" + cssClass + "\">" + "#"+tag.getTagname() + "</span>";
		}).collect(Collectors.joining());
	}

	private String getTagCssClass(String tagname) {
		if (tagname.equals("MongoDB") || tagname.equals("PostgreSQL") || tagname.equals("SQLServer")|| tagname.equals("MySQL")) {
			return "tagDatabase";
		} else if (tagname.equals("Angular")|| tagname.equals("CSS")
				|| tagname.equals("SCSS")|| tagname.equals("Sass")
				|| tagname.equals("JavaScript")|| tagname.equals("jQuery")
				|| tagname.equals("Vue")|| tagname.equals("React")) {
			return "tagFront";
		} else {
			return "tagBack";
		}
	}

	public void setTags(Set<yuTag> tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Long collectCount) {
		this.collectCount = collectCount;
	}

	
}
