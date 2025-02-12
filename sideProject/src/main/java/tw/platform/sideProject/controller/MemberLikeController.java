package tw.platform.sideProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.repository.MemberRepository;



@Controller
public class MemberLikeController {
    
	@Autowired
    private MemberRepository memberRepository;
	
	@GetMapping("/memberlike/{memberid}")
    public String getMemberlikePage(@PathVariable Long memberid, Model model) {
        model.addAttribute("memberid", memberid); // 将 memberid 传递给前端
        Optional<Member> memberOpt = memberRepository.findById(memberid);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            model.addAttribute("member", member);
        }
        return "memberlike"; // 返回的是位于 static 文件夹下的 memberProjectP1.html
    }
    
}