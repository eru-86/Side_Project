package tw.platform.sideProject.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.model.Order;
import tw.platform.sideProject.model.mimiMember;
import tw.platform.sideProject.repository.MemberRepository;
import tw.platform.sideProject.repository.mimiMemberRepository;
import tw.platform.sideProject.service.MemberOrderService;
import tw.platform.sideProject.service.MemberService;

@Controller
public class ProjectController {
	@Autowired
	private mimiMemberRepository memberRepository;

	@Autowired
	private MemberRepository mimimemberRepository;

	@Autowired
	private MemberService memberService;

	@GetMapping("/memberHome")
	public String showMemberHomePage(HttpSession session, Model model) {
		System.out.println("思宇登入後的後端");
		// 从 session 获取会员对象
		mimiMember sessionMember = (mimiMember) session.getAttribute("member");
		model.addAttribute("member", sessionMember);
		session.setAttribute("member", sessionMember);

		return "memberHome"; // 返回视图
	}

	@Transactional
	@PostMapping("/memberHome/{memberid}/update")
	public String updateMember(@PathVariable("memberid") Long memberid,
			@RequestParam(value = "picurl", required = false) String picurl, @ModelAttribute Member member,
			HttpSession session, Model model) throws IOException {
		System.out.println("儲存後端");
//	    System.out.println("Received picurl: " + picurl); // Debugging

		// 查找数据库中的会员数据
		mimiMember sessionMember = (mimiMember) session.getAttribute("member");

		// 更新其他字段（检查是否为空，如果不为空才进行更新）
		if (member.getAccount() != null && !member.getAccount().isEmpty()) {
			sessionMember.setAccount(member.getAccount());
		}
		if (member.getEmail() != null && !member.getEmail().isEmpty()) {
			sessionMember.setEmail(member.getEmail());
		}
		if (member.getPassword() != null && !member.getPassword().isEmpty()) {
			sessionMember.setPassword(member.getPassword());
		}
		if (member.getTel() != null && !member.getTel().isEmpty()) {
			sessionMember.setTel(member.getTel());
		}

		if (member.getGithub() != null && !member.getGithub().isEmpty()) {
			sessionMember.setGithub(member.getGithub());
		}

		if (member.getBirthday() != null) {
			sessionMember.setBirthday(member.getBirthday());
		}
		if (member.getIntro() != null && !member.getIntro().isEmpty()) {
			sessionMember.setIntro(member.getIntro());
		}

		// 处理图片上传（已直接传递 Base64 字符串）
		if (picurl != null && !picurl.isEmpty()) {
			// 直接将 Base64 字符串赋值给 picurl 字段
			sessionMember.setPicurl(picurl);
		}

		// 更新数据库中的会员数据
		memberRepository.save(sessionMember);

		// 将更新后的数据添加到 model 中，以便返回到前端
		model.addAttribute("member", sessionMember);
		session.setAttribute("member", sessionMember);
		System.out.println(sessionMember);
		// 返回更新后的页面

		return "redirect:/memberHome"; // 返回更新后的页面
	}

	// =========================================//
//	@GetMapping("/memberProjectP2/{memberid}")
//	public String getMemberProjectPage2(@PathVariable Long memberid, Model model) {
//		model.addAttribute("memberid", memberid); // 将 memberid 传递给前端
//		return "memberProjectP2"; // 返回的是位于 static 文件夹下的 memberProjectP1.html
//	}
	// 我接案的跳轉後端
	@GetMapping("/memberProjectP1/{memberid}")
	public String getMemberProjectPage(@PathVariable Long memberid, Model model) {
		model.addAttribute("memberid", memberid); // 将 memberid 传递给前端
		Optional<Member> memberOpt = mimimemberRepository.findById(memberid);
		if (memberOpt.isPresent()) {
			Member member = memberOpt.get();
			model.addAttribute("member", member);
		}
		return "memberProjectP1"; // 返回的是位于 static 文件夹下的 memberProjectP1.html
	}

	@GetMapping("/memberProjectP2/{memberid}")
	public String getMemberProjectPage2(@PathVariable Long memberid, Model model) {
		model.addAttribute("memberid", memberid); // 将 memberid 传递给前端
		return "memberProjectP2"; // 返回的是位于 static 文件夹下的 memberProjectP1.html
	}

	@GetMapping("/OrderProjectP1/{memberid}")
	public String getOrderProjectPage(@PathVariable Long memberid, Model model) {
		model.addAttribute("memberid", memberid); // 将 memberid 传递给前端
		Optional<Member> memberOpt = mimimemberRepository.findById(memberid);
		if (memberOpt.isPresent()) {
			Member member = memberOpt.get();
			model.addAttribute("member", member);
		}
		return "OrderProjectP1"; // 返回的是位于 static 文件夹下的 memberProjectP1.html
	}

	@GetMapping("/getMembersByorderid/{orderid}")
	public String getorderByorderid(@PathVariable Long orderid, Model model) {
		model.addAttribute("orderid", orderid); // 将 memberid 传递给前端

		return "evaluate"; // 返回的是位于 static 文件夹下的 memberProjectP1.html
	}

}