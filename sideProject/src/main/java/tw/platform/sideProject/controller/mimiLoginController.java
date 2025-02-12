package tw.platform.sideProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import tw.platform.sideProject.model.Keywords;
import tw.platform.sideProject.model.Message;
import tw.platform.sideProject.model.mimiMember;
import tw.platform.sideProject.model.mimiTag;
import tw.platform.sideProject.repository.mimiTagRepository;
import tw.platform.sideProject.service.KeywordService;
import tw.platform.sideProject.service.mimiMemberService;

@Controller
public class mimiLoginController {

	@Autowired
	private mimiMemberService memberService;

	@Autowired
	private mimiTagRepository tagRepository;

	@Autowired
	private KeywordService keywordService;

	@RequestMapping("/register")
	public String reg(Model model, HttpSession session) {
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("index目前登入狀態:" + member.getName() + "%n");
			model.addAttribute("member", member);
		}

//		List<String> options = List.of("python","java","C#","Mysql","CSS");
		List<mimiTag> options = tagRepository.findAll();

		model.addAttribute("newMember", new mimiMember());
		model.addAttribute("options", options);

//		System.out.println(options.toString());

		return "register";
	}

	@PostMapping("/reg_submit")
	public String regSubmit(@ModelAttribute mimiMember member,
			@RequestParam(value = "selected", required = false) List<Long> selected, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors().toString());
			return "register";
		}

		// checkbox
		if (selected != null) {
			List<mimiTag> tags = tagRepository.findAllById(selected);
			member.setTags(List.copyOf(tags));
		}

		System.out.println(member.getAccount());
		System.out.println(member.getPassword());
		System.out.println(member.getName());
		System.out.println(member.getEmail());
		System.out.println("selected:" + member.getTags());

		memberService.addMember(member);
		model.addAttribute("newMember", member);

		return "login";
	}

	@GetMapping("/login")
	public String login(Model model, HttpSession session) {
		System.out.println("進入login");
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("index目前登入狀態:" + member.getName() + "%n");
			model.addAttribute("member", member);
		}
		model.addAttribute("newMember", new mimiMember());
		return "login";
	}

	@PostMapping("/login_submit")
	public String loginSubmit(@ModelAttribute("member") mimiMember member, BindingResult result, Model model,
			HttpSession session) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors().toString());
			System.out.println("debug1");
			model.addAttribute("newMember", member);
			return "login";
		}
		System.out.println(member.getEmail());
		System.out.println(member.getPassword());

		member = memberService.loginMember(member);
		if (member == null) {
			System.out.println("debug2");
			model.addAttribute("errorMessage", "Email或密碼錯誤");
			model.addAttribute("newMember", new mimiMember());// 登入失敗給一個空的newMember
			return "login";
		} else if (member.isBlocked()) {
			System.out.println("此帳號無法登入");
			model.addAttribute("errorMessage", "此帳號無法登入");
			model.addAttribute("newMember", new mimiMember());// 登入失敗給一個空的newMember
			return "login";
		} else {
			session.setAttribute("member", member);// 登入成功後帶入的會員資料
		}
		Message message = new Message();
		model.addAttribute("message", message); // 添加空的Message對象到模型
		model.addAttribute("member", member);// 登入成功後帶入的會員資料
		session.setAttribute("message", message);
		return "redirect:/memberHome"; //思宇的會員中心
//		return "redirect:/main/home";
//		return "memberShowBtn";
	}

	@PostMapping("/logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		List<Keywords> list = keywordService.getKeywordDesc();
		model.addAttribute("kw", list);
		return "redirect:/index";
	}
}
