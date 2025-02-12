package tw.platform.sideProject.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class yuMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberid;

	private String account;
	private String password;
	private String email;
	private String name;
	private String birthday;
	private String tel;
	private String intro;
	private String github;
	private String picurl;
	@Column(nullable = false)
	private Boolean isblocked = false; // 預設值

	@ManyToMany
	@JoinTable(name = "membertag", joinColumns = @JoinColumn(name = "memberid"), inverseJoinColumns = @JoinColumn(name = "tagid"))
	private Set<yuTag> tags = new HashSet<>();

	// Getters and Setters
	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public Boolean getIsblocked() {
		return isblocked;
	}

	public void setIsblocked(Boolean isblocked) {
		this.isblocked = isblocked;
	}

	public String getTagNames() {
		return tags.stream().map(tag -> {
			String cssClass = getTagCssClass(tag.getTagname());
			return "<span class=\"" + cssClass +" textCSS\">" + "#"+tag.getTagname() + "</span>";
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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getGithub() {
		return github;
	}

	public void setGithub(String github) {
		this.github = github;
	}

}
