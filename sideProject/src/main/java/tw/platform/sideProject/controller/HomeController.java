package tw.platform.sideProject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import tw.platform.sideProject.model.Keywords;
import tw.platform.sideProject.model.Message;
import tw.platform.sideProject.model.mimiMember;
import tw.platform.sideProject.model.yuMember;
import tw.platform.sideProject.model.yuOrder;
import tw.platform.sideProject.service.KeywordService;
import tw.platform.sideProject.service.MemberService;
import tw.platform.sideProject.service.OrderService;
import tw.platform.sideProject.service.mimiMemberService;

@Controller
public class HomeController {

	@Autowired
	private KeywordService keywordService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private mimiMemberService mimiMemberService;

	@GetMapping("/index")
	public String index(Model model, HttpSession session) {
		System.out.println("---home---");
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("index目前登入狀態:" + member.getName());
			model.addAttribute("member", member);
			List<yuMember> ranMembers = memberService.getRandomYuMembers2(member.getMemberid());
			for (yuMember member2 : ranMembers) {
				if (member2.getPicurl() == null || member2.getPicurl() == "") {
					member2.setPicurl("../img/memberImg.jpg");
					System.out.println(member2.getMemberid());
				}
			}
			model.addAttribute("ranMembers", ranMembers);
		} else {
			System.out.println("index訪客模式");
			// 抓取隨機會員資料
			List<yuMember> ranMembers = memberService.getRandomYuMembers();
			for (yuMember member : ranMembers) {
				if (member.getPicurl() == null || member.getPicurl() == "") {
					member.setPicurl("../img/memberImg.jpg");
					System.out.println(member.getMemberid());
				}
			}
			model.addAttribute("ranMembers", ranMembers);
		}
		// 抓取熱門申請專案
		List<Object[]> owentLise = orderService.getWantedCountByOrderId();
		List<yuOrder> wentOrders = new ArrayList<>();
		List<Long> wantCounts = new ArrayList<>();
		int caseCouunt = 0;
		for (Object[] lists : owentLise) {
			if (caseCouunt >= 3) {
				break;
			}
			yuOrder order = (yuOrder) lists[0];
			Long want = (Long) lists[1];
			System.out.println("訂單: " + order.getName() + ", 申請人數: " + lists[1] + ",截止日期:" + order.getDeadline());
			wentOrders.add(order);
			wantCounts.add(want);
			caseCouunt++;
		}
		model.addAttribute("keywordCase", wentOrders);
		model.addAttribute("wantCounts", wantCounts);
		// 抓取隨機關鍵字
		List<Keywords> list = keywordService.getKeywordDesc();
		model.addAttribute("kw", list);

		return "index";
	}

	@GetMapping("/order_main/{orderId}")
	public String order_main(@PathVariable Long orderId, Model model, HttpSession session) {
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("index目前登入狀態:" + member.getName() + "%n");
			model.addAttribute("member", member);
			model.addAttribute("memberId", member.getMemberid());
		} else {
			System.out.println("目前無登入紀錄");
		}

		model.addAttribute("message", new Message());
		model.addAttribute("orderId", orderId);
		return "order_main";
	}

	@GetMapping("/order_edit")
	public String order_edit(Model model, HttpSession session) {
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("index目前登入狀態:" + member.getName() + "%n");
			model.addAttribute("member", member);
			model.addAttribute("memberId", member.getMemberid());
		} else {
			System.out.println("目前無登入紀錄");
		}

		return "order_edit";
	}

	@GetMapping("/order_update/{orderId}")
	public String order_update(@PathVariable Long orderId, Model model, HttpSession session) {
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("index目前登入狀態:" + member.getName() + "%n");
			model.addAttribute("member", member);
			model.addAttribute("memberId", member.getMemberid());
		} else {
			System.out.println("目前無登入紀錄");
		}

		model.addAttribute("orderId", orderId);
		return "order_update";
	}

	// 搜尋鈕
	@GetMapping("/search")
	public String search(Model model, HttpSession session) {
		System.out.println("---搜尋---");
		// 判斷是否有登入狀態
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("search目前登入狀態:" + member.getName());
			model.addAttribute("member", member);
		} else {
			System.out.println("search訪客模式");
		}

		// 隨機關鍵字
		List<Keywords> kwlist = keywordService.getKeywordDesc();
		model.addAttribute("kw", kwlist);

		// 從session拿取搜尋邏輯的結果
		@SuppressWarnings("unchecked")
		List<yuOrder> keywordCase = (List<yuOrder>) session.getAttribute("keywordCase");
		if (keywordCase != null) {
			System.out.println("test :" + keywordCase);
			for (yuOrder order : keywordCase) {
				// 如果訂單沒圖片，加入圖片
				if (order.getPicurl() == null) {
					order.setPicurl("../img/caseImg.jpg");
				}
			}
			model.addAttribute("keywordCase", keywordCase);
		} else {
			System.out.println("注入失敗");
		}
		session.setAttribute("keywordCase", keywordCase);
		return "search";
	}

	// 找case進入
	@GetMapping("/searchCase")
	public String searchCase(Model model, HttpSession session) {
		System.out.println("---進入searchCase---");
		// 判斷是否有登入狀態
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("searchCase目前登入狀態:" + member.getName());
			model.addAttribute("member", member);
		} else {
			System.out.println("searchCase訪客模式");
		}

		// 隨機關鍵字
		List<Keywords> kwlist = keywordService.getKeywordDesc();
		model.addAttribute("kw", kwlist);

		// 抓取隨機專案
		List<yuOrder> ranOrders = orderService.getRandomYuOrders();
		for (yuOrder order : ranOrders) {
			if (order.getPicurl() == null) {
				order.setPicurl("../img/caseImg.jpg");
			}
		}
		model.addAttribute("keywordCase", ranOrders);
		session.setAttribute("keywordCase", ranOrders);
		return "search";
	}

	@GetMapping("/orderShow")
	public String orderShow(@RequestParam Long orderid, Model model) {
		System.out.println("前端傳送的專案ID : " + orderid);
		List<yuOrder> orderShow = orderService.getyuOrderById(orderid);
		for (yuOrder orderCheck : orderShow) {
			// 如果訂單沒圖片，加入圖片
			if (orderCheck.getPicurl() == null) {
				orderCheck.setPicurl("../img/caseImg.jpg");
			}
		}
		model.addAttribute("orderShow", orderShow);
		return "orderShow";
	}

	@PostMapping("/memberShow")
	public String memberShow(@RequestParam Long memberid, Model model, HttpSession session) {
		System.out.println("前端傳送的會員ID : " + memberid);
		// 判斷是否有登入狀態
		if (session.getAttribute("member") != null) {
			mimiMember member = (mimiMember) session.getAttribute("member");
			System.out.println("memberShow目前登入狀態:" + member.getName());
			model.addAttribute("member", member);
		} else {
			System.out.println("searchCase訪客模式");
		}
		List<yuMember> memberShow = memberService.getyuMemberById(memberid);
		for (yuMember memberCheck : memberShow) {
			System.out.println("前端查詢的會員name : " +memberCheck.getName());
			System.out.println("前端查詢的會員Email : " +memberCheck.getEmail());

			// 如果訂單沒圖片，加入圖片
			if (memberCheck.getPicurl() == null) {
				memberCheck.setPicurl("../img/caseImg.jpg");
			}

			// 假設這裡的 receiverid 是 mimiMember 類型
			mimiMember receiverMember = mimiMemberService.getMemberById(memberid); // 通過 memberid 查詢 mimiMember
			String receiverEmail = memberCheck.getEmail(); // 取得該會員的 Email

			// 創建 Message 物件並設置 receiverid
			Message message = new Message();
			message.setReceiverid(receiverMember); // 設置 receiverid 為 mimiMember 物件

			// 設置收件人 Email
			message.setReceiverEmail(receiverEmail); // 設置收件人的 Email

			model.addAttribute("message", message); // 添加 Message 物件到模型
		}
		model.addAttribute("memberShow", memberShow);
		session.setAttribute("message", new Message()); // 把空的 Message 存到 Session
		return "memberShow";
	}

	// --------return視圖使用---------------
	@GetMapping("/memberShow")
	public String getmemberShow(@RequestParam Long memberid, Model model, HttpSession session) {

		System.out.println("前端傳送的會員ID : " + memberid);
		List<yuMember> memberShow = memberService.getyuMemberById(memberid);
		for (yuMember memberCheck : memberShow) {
			System.out.println(memberCheck.getName());
			System.out.println(memberCheck.getEmail());

			if (memberCheck.getPicurl() == null) {
				memberCheck.setPicurl("../img/caseImg.jpg");
			}
			// get receiverMember
			mimiMember receiverMember = mimiMemberService.getMemberById(memberid);
			String receiverEmail = memberCheck.getEmail();
			// Message 表單置入 receiverid 的 email
			Message message = new Message();
			message.setReceiverid(receiverMember);
			message.setReceiverEmail(receiverEmail);
			model.addAttribute("message", message);
		}
		model.addAttribute("memberShow", memberShow);
		session.setAttribute("message", new Message());
		return "memberShow";
	}

	// --------彈窗測試項目-----------
	@GetMapping("/indexText")
	public String indexText(Model model, HttpSession session) {
		return "memberShowBtn";
	}

	// 彈窗後端
	@GetMapping("/caseMember")
	@ResponseBody
	public yuMember caseMember(@RequestParam("memberid") Long memberid) {
		System.out.println("前端傳送的會員ID : " + memberid);
		List<yuMember> caseMember = memberService.getyuMemberById(memberid);
		for (yuMember memberCheck : caseMember) {
			System.out.println(memberCheck.getName());
			if (memberCheck.getPicurl() == null) {
				memberCheck.setPicurl("../img/caseImg.jpg");
			}
		}
		return caseMember.isEmpty() ? null : caseMember.get(0);
	}

}
