package tw.platform.sideProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import tw.platform.sideProject.model.AddMemberRequest;
import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.model.MemberTag;
import tw.platform.sideProject.model.MemberTagKey;
import tw.platform.sideProject.model.Tag;
import tw.platform.sideProject.model.yuMember;
import tw.platform.sideProject.repository.MemberRepository;
import tw.platform.sideProject.repository.MemberTagRepository;
import tw.platform.sideProject.repository.TagRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private MemberTagRepository memberTagRepository;

	// 新增會員
	public String addMember(@RequestBody AddMemberRequest request) {
		// 建立並儲存會員
		Member member = new Member();
		member.setAccount(request.getAccount());
		member.setPassword(request.getPassword());
		member.setEmail(request.getEmail());
		member.setName(request.getName());
		member.setBirthday(request.getBirthday());
		member.setTel(request.getTel());
		member.setPicurl(request.getPicurl());
		member.setIsblocked(false);
		memberRepository.save(member);

		// 處理傳入的 tagIds，建立 MemberTag 資料
		List<MemberTag> memberTags = new ArrayList<>();
		for (Integer tagId : request.getTagIds()) {
			// 查找對應的 Tag
			Tag tag = tagRepository.findById(tagId)
					.orElseThrow(() -> new RuntimeException("Tag not found for ID: " + tagId));

			// 建立 MemberTagKey 作為複合主鍵
			MemberTagKey memberTagKey = new MemberTagKey();
			memberTagKey.setMemberId(member.getMemberid());
			// memberTagKey.setTagId(tag.getTagidm());

			// 建立 MemberTag 並設置關聯
			MemberTag memberTag = new MemberTag();
			memberTag.setId(memberTagKey);
//            memberTag.setMember(member);
			memberTag.setTag(tag);

			memberTags.add(memberTag);
		}

		// 儲存所有的 MemberTag
		memberTagRepository.saveAll(memberTags);

		return "會員新增成功，並建立對應的標籤關聯";

	}

	/**
	 * 查詢所有會員
	 * 
	 * @return List<Member> 會員列表
	 */
	public List<Member> getAllMembers() {
		return memberRepository.findAll();
	}

	/**
	 * 根據 Email 查詢會員
	 * 
	 * @param email 會員 Email
	 * @return Optional<Member> 匹配的會員
	 */
	public Optional<Member> getMemberByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	/**
	 * 根據 ID 更新會員資料
	 * 
	 * @param memberId      會員 ID
	 * @param memberDetails 包含更新內容的 Member 物件
	 * @return 更新後的 Member 物件
	 */
	public Member updateMember(Long memberId, Member memberDetails) {
		return memberRepository.findById(memberId).map(existingMember -> {
			// 僅更新非空欄位
			if (memberDetails.getAccount() != null) {
				existingMember.setAccount(memberDetails.getAccount());
			}
			if (memberDetails.getPassword() != null) {
				existingMember.setPassword(memberDetails.getPassword());
			}
			if (memberDetails.getEmail() != null) {
				existingMember.setEmail(memberDetails.getEmail());
			}
			if (memberDetails.getPicurl() != null) {
				existingMember.setPicurl(memberDetails.getPicurl());
			}
			if (memberDetails.getName() != null) {
				existingMember.setName(memberDetails.getName());
			}
			if (memberDetails.getBirthday() != null) {
				existingMember.setBirthday(memberDetails.getBirthday());
			}
			if (memberDetails.getTel() != null) {
				existingMember.setTel(memberDetails.getTel());
			}
			if (memberDetails.getIsblocked() != null) {
				existingMember.setIsblocked(memberDetails.getIsblocked());
			}
			// 儲存更新後的資料
			return memberRepository.save(existingMember);
		}).orElseThrow(() -> new RuntimeException("會員 ID 不存在: " + memberId));
	}

	/**
	 * 根據會員 ID 更新標籤
	 *
	 * @param memberId 會員 ID
	 * @param tagIds   要更新的標籤 ID 列表
	 * @return 更新結果的訊息
	 */
	public String updateMemberTags(Long memberId, List<Integer> tagIds) {
		// 查找會員是否存在
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new RuntimeException("會員 ID 不存在: " + memberId));

		// 刪除該會員現有的所有 MemberTag
		memberTagRepository.deleteByMemberId(memberId);

		// 新增新的標籤關聯
		List<MemberTag> memberTags = new ArrayList<>();
		for (Integer tagId : tagIds) {
			// 查找對應的 Tag
			Tag tag = tagRepository.findById(tagId)
					.orElseThrow(() -> new RuntimeException("Tag not found for ID: " + tagId));

			// 建立 MemberTagKey 作為複合主鍵
			MemberTagKey memberTagKey = new MemberTagKey();
			memberTagKey.setMemberId(memberId);
			// memberTagKey.setTagId(tag.getTagidm());

			// 建立 MemberTag 並設置關聯
			MemberTag memberTag = new MemberTag();
			memberTag.setId(memberTagKey);
			memberTag.setMember(member);
			memberTag.setTag(tag);

			memberTags.add(memberTag);
		}

		// 儲存所有的 MemberTag
		memberTagRepository.saveAll(memberTags);

		return "會員的標籤已成功更新";
	}

	/**
	 * 根據 ID 刪除會員
	 * 
	 * @param memberId 會員 ID
	 */
	public void deleteMember(Long memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw new RuntimeException("會員 ID 不存在: " + memberId);
		}
		memberRepository.deleteById(memberId);
	}

//	YU新增
//	首頁隨抓取會員資料
	public List<yuMember> getRandomYuMembers() {
		List<yuMember> allMembers = memberRepository.findRandomYuMembers();
		// 排除掉 ID 為 1 (發信系統) 的會員並只取前 4 個
		return allMembers.stream().filter(member -> member.getMemberid() != 1).limit(4).collect(Collectors.toList());
	}

	public List<yuMember> getRandomYuMembers2(Long memberid) {
		List<yuMember> allMembers = memberRepository.findRandomYuMembers();
		// 排除掉 ID 為 1 (發信系統) 與登入者的會員並只取前 4 個
		return allMembers.stream().filter(member -> member.getMemberid() != 1 && member.getMemberid() != memberid)
				.limit(4).collect(Collectors.toList());
	}

	public List<yuMember> getyuMemberById(Long memberid) {
		return memberRepository.findYuMemberByMemberId(memberid);
	}

}