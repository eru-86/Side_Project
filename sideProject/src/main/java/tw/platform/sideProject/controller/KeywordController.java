package tw.platform.sideProject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpSession;
import tw.platform.sideProject.model.SearchRequest;
import tw.platform.sideProject.model.yuOrder;
import tw.platform.sideProject.service.KeywordService;
import tw.platform.sideProject.service.OrderService;

@Controller
public class KeywordController {

	@Autowired
	private KeywordService keywordService;

	@Autowired
	private OrderService orderService;

	// 針對關鍵字進行搜尋，並將搜尋資料回傳至fragments的keywordsearchBox
	@PostMapping("/SaveAndSearchKeyword")
	@ResponseBody // 回傳片段
	public ResponseEntity<?> SaveAndSearchKeyword(@RequestBody SearchRequest searchRequest, Model model,
			HttpSession session) {
		String userKeyword = searchRequest.getUserKeyword();
		List<String> options = searchRequest.getOptions();

		List<yuOrder> list = new ArrayList<>();

		System.out.println("-----------");
		System.out.println("進入存+搜關鍵字");

		if (userKeyword != "") {
			// 存關鍵字邏輯
			System.out.println("使用者輸入: " + userKeyword);
			keywordService.saveKeyword(userKeyword);
			// 查詢關鍵字
			list = keywordService.searchKeywords(userKeyword);
			System.out.println("根據關鍵詞 " + userKeyword + " 搜尋共有 " + list.size() + " 筆資料");
		} else {
			// 無關鍵字，查詢所有專案
			list = orderService.getAllYuOrders();
			System.out.println("無關鍵字輸入，查詢所有專案");
		}

		if (options == null) {
			// options為null，則給一個空的陣列
			options = new ArrayList<>();
			// 確認一下搜尋到的資料(都完成後可刪除)
			for (yuOrder order : list) {
				String tagNames = order.getTagNames();
				System.out.printf("無options，符合關鍵字查詢的專案為 : 專案id %d.專案名稱:%s%n", order.getOrderid(), order.getName());
				// 將這邊的結果存到session，帶至搜尋頁
				session.setAttribute("keywordCase", list);
			}
		} else {
			// options不為null則比對查詢到的結果
			List<yuOrder> userList = new ArrayList<>();
			for (yuOrder order : list) {
				for (String option : options) {
					if (order.getTagNames().contains(option)
							|| order.getLocation() != null && order.getLocation().contains(option)
							|| order.getCategory() != null && order.getCategory().contains(option)) {
						userList.add(order);
						if (order.getTagNames().contains(option)) {
							System.out.println("專案所需技能: " + order.getTagNames());
						} else if (order.getLocation().contains(option)) {
							System.out.println("專案所在地: " + order.getLocation());
						} else {
							System.out.println("專案類型: " + order.getCategory());
						}
						break; // 若找到tag與location與使用者輸入符合的專案即加入並退出內層循環
					}
				}
			}
			System.out.println("符合關鍵字+options查詢的訂單: " + userList.size());
			// 確認一下搜尋到的資料(都完成後可刪除)
			for (yuOrder order : userList) {
				String tagNames = order.getTagNames();
				System.out.printf("專案id %d.專案名稱:%s,類型:%s,地點:%s,標籤:%s,簡介:%s%n", order.getOrderid(), order.getName(),
						order.getCategory(), order.getLocation(), tagNames, order.getIntro());
				// 將這邊的結果存到session，帶至搜尋頁
				session.setAttribute("keywordCase", userList);
			}
		}

		return ResponseEntity.ok("處理成功");
	}

	@GetMapping("/randomKeyword")
	public ResponseEntity<String> getRandomKeyword() {
		String randomKeyword = keywordService.getRandomKeyword();
		return new ResponseEntity<>(randomKeyword, HttpStatus.OK);
	}

	// 針對關鍵字搜尋到的專案進行日期排序
	@GetMapping("/caseTimeDSC")
	public String caseTimeDSC(Model model, HttpSession session) {
		System.out.println("進入到期日排序後端");
		@SuppressWarnings("unchecked")
		List<yuOrder> caseTime = (List<yuOrder>) session.getAttribute("keywordCase");

		keywordService.timeSortDSC(caseTime);
		for (yuOrder order : caseTime) {
			System.out.println("專案名稱:" + order.getName() + ",到期日" + order.getDeadline());
		}
		session.setAttribute("keywordCase", caseTime);
		model.addAttribute("keywordCase", caseTime);
		System.out.println("注入完成");
		return "search::#keywordsearchBox";
	}

	@GetMapping("/caseTimeASC")
	public String caseTimeASC(Model model, HttpSession session) {
		System.out.println("進入到期日排序後端");
		@SuppressWarnings("unchecked")
		List<yuOrder> caseTime = (List<yuOrder>) session.getAttribute("keywordCase");

		keywordService.timeSortASC(caseTime);
		for (yuOrder order : caseTime) {
			System.out.println("專案名稱:" + order.getName() + ",到期日" + order.getDeadline());
		}
		session.setAttribute("keywordCase", caseTime);
		model.addAttribute("keywordCase", caseTime);
		System.out.println("注入完成");
		return "search::#keywordsearchBox";
	}

	// 針對關鍵字搜尋到的專案進行收藏數排序
	@GetMapping("/caseCollected")
	public String caseWant(Model model, HttpSession session) {

		System.out.println("進入到收藏排序後端");
		@SuppressWarnings("unchecked")
		List<yuOrder> caseCollected = (List<yuOrder>) session.getAttribute("keywordCase");

		// 針對搜尋到的專案獲取每個訂單的收藏人數
		for (yuOrder order : caseCollected) {
			Long collectCount = orderService.getCollectedCountByOrderId(order.getOrderid());
			order.setCollectCount(collectCount);
		}
		// 按收藏人數對列表進行排序
		keywordService.getCollectedSort(caseCollected);
		for (yuOrder order : caseCollected) {
			System.out.println("專案名稱:" + order.getName() + ",收藏人數:" + order.getCollectCount());
		}

		session.setAttribute("keywordCase", caseCollected);
		model.addAttribute("keywordCase", caseCollected);
		System.out.println("注入完成");
		return "search::#keywordsearchBox";
	}

	@GetMapping("/getOrderID")
	public String getOrderID(@RequestParam Long orderID) {
		System.out.println("前端傳送的ID : " + orderID);
		return "memberShow";
	}

}
