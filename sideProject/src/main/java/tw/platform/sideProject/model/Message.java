package tw.platform.sideProject.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageid;
	
	@ManyToOne//根據Memberid匯入senderid
	@JoinColumn(name = "senderid")
	private mimiMember senderid;
	
	@ManyToOne//根據Memberid匯入receiverid
	@JoinColumn(name = "receiverid")
	private mimiMember receiverid;
	@NotBlank(message = "不可為空")
	private String title;
	@NotBlank(message = "不可為空")
	private String content;
	
    @CreationTimestamp
    @Column(name = "createdat",nullable = false, updatable = false) // 如果資料庫欄位名稱是 created_at，則使用 @Column 映射
    private LocalDateTime createdat;
	
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private boolean isread = false;
//----------------	
	@Transient
	private String isreadText;

//----------------
	private String receiverEmail;
	
	
	public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
//----------------
	
	public String getIsreadText() {
		return isreadText;
	}

	public void setIsreadText(String isreadText) {
		this.isreadText = isreadText;
	}

	//------isread----------
	public boolean isIsread() {
		return isread;
	}
	
	public void setIsread(boolean isread) {
		this.isread = isread;
	}
	
	//--------------
	public mimiMember getSenderid() {
		return senderid;
	}


	public void setSenderid(mimiMember senderid) {
		this.senderid = senderid;
	}

	public mimiMember getReceiverid() {
		return receiverid;
	}

	public void setReceiverid(mimiMember receiverid) {
		this.receiverid = receiverid;
	}

	public Long getMessageid() {
		return messageid;
	}

	public void setMessageid(Long messageid) {
		this.messageid = messageid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedat() {
		return createdat;
	}

	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}

	
	
}
