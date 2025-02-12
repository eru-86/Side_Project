package tw.platform.sideProject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
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
public class mimiMember {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberid;
	private String account;
	private String password;
	private String name;
	private String tel;
	private String email;
	private String picurl;
	private String birthday;
	private String intro;
	private String github;
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isblocked = false;
	
	
	
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
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public boolean isIsblocked() {
		return isblocked;
	}
	public void setIsblocked(boolean isblocked) {
		this.isblocked = isblocked;
	}
	public boolean isBlocked() {
		return isblocked;
	}
	public void setBlocked(boolean isblocked) {
		this.isblocked = isblocked;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
//-------------------------------------------
	//多個會員多個tag
	@ManyToMany
	@JoinTable(name = "membertag", 
			joinColumns = @JoinColumn(name = "memberid"),
			inverseJoinColumns = @JoinColumn(name = "tagid"))
	private List<mimiTag> tags;


	public List<mimiTag> getTags() {
		return tags;
	}
	public void setTags(List<mimiTag> tags) {
		this.tags = tags;
	}
	
	
	
}
