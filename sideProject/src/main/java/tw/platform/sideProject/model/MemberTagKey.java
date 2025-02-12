package tw.platform.sideProject.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class MemberTagKey implements Serializable {

	private Long memberId;
    private int tagId;

    // Getters and Setters
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    // Equals and hashCode 方法（需覆寫）
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MemberTagKey that = (MemberTagKey) obj;
        return tagId == that.tagId && memberId.equals(that.memberId);
    }

    @Override
    public int hashCode() {
        return 31 * memberId.hashCode() + tagId;
    }
}