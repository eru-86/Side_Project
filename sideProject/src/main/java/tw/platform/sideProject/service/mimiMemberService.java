package tw.platform.sideProject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.platform.sideProject.model.mimiMember;
import tw.platform.sideProject.repository.mimiMemberRepository;
import tw.platform.sideProject.util.BCrypt;

@Service
public class mimiMemberService {

	@Autowired
	private mimiMemberRepository memberRepository;

	public void addMember(mimiMember member) {
		member.setPassword(BCrypt.hashpw(member.getPassword(), BCrypt.gensalt()));
		mimiMember saveMember = memberRepository.save(member);// 表單資料
	}

	public mimiMember loginMember(mimiMember loginmember) {
		Optional<mimiMember> opt = memberRepository.findByEmail(loginmember.getEmail());
		try {
			mimiMember member = opt.get();
			if (member != null) {
				System.out.println("debug3");
				if (!BCrypt.checkpw(loginmember.getPassword(), member.getPassword())) {
					member = null;
					System.out.println("debug4");
				} else {
				} // iconbyte[]陣列轉base64
			}
			return member;
		} catch (Exception e) {
			return null;
		}
	}

	// Long
	public Long getMemberid(Long memberid) {
		mimiMember member = memberRepository.findById(memberid)
				.orElseThrow(() -> new RuntimeException("Member not found"));
		return member.getMemberid();
	}
	//mimiMember
	public mimiMember getMemberById(Long memberid) {
        // 假設 mimiMemberRepository 有一個 findById 方法
        return memberRepository.findById(memberid).orElse(null); // 返回找到的會員，若找不到則返回 null
    }
}
