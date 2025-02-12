package tw.platform.sideProject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "membertag")
public class MemberTag {

	 @EmbeddedId
	 private MemberTagKey id;

	 @ManyToOne
	 @MapsId("memberId")
	 @JsonBackReference
	 @JoinColumn(name = "memberid")
	 private Member member;

	 @ManyToOne
	 @MapsId("tagId")
	 @JsonBackReference
	 @JoinColumn(name = "tagid")
	 private Tag tag;

	 // Getters and Setters
	 public MemberTagKey getId() {
		 return id;
	 }

	 public void setId(MemberTagKey id) {
		 this.id = id;
	 }

	    public Member getMember() {
	        return member;
	    }

	 public void setMember(Member member) {
	     this.member = member;
	 }

	 public Tag getTag() {
	     return tag;
	 }

	 public void setTag(Tag tag) {
	     this.tag = tag;
	 }
}
