package tw.platform.sideProject.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import tw.platform.sideProject.model.AddMemberOrderRequest;
import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.model.MemberOrder;
import tw.platform.sideProject.model.Order;
import tw.platform.sideProject.model.UpdateFavoriteRequest;
import tw.platform.sideProject.repository.MemberOrderRepository;
import tw.platform.sideProject.service.MemberOrderService;

// Controller 只處理當 request 來用 response 回應 => 再給 Service 處理邏輯 => 再給 Dao 與資料庫溝通，存取物件
@RestController
@RequestMapping("/api/memberOrders")
@CrossOrigin(origins = "*") // 避免 CORS 問題
public class MemberOrderController {

	@Autowired
	private MemberOrderService memberOrderService;

	// 新增會員創建專案的關聯 (owned設為true)
	@PostMapping("/addOwnedOrder")
	public String addOwnedOrder(@RequestBody AddMemberOrderRequest request) {
		return memberOrderService.addOwnedOrder(request);
	}

	// 新增會員對專案有興趣的關聯 (wanted設為true)
	@PostMapping("/addWantedOrder")
	public String addWantedOrder(@RequestBody AddMemberOrderRequest request) {
		return memberOrderService.addWantedOrder(request);
	}

	// 移除會員對專案有興趣的關聯 (wanted設為false)
	@DeleteMapping("/removeWantedOrder/{memberId}/{orderId}")
	public String removeWantedOrder(@PathVariable Long memberId, @PathVariable Long orderId) {
		return memberOrderService.removeWantedOrder(memberId, orderId);
	}

	// 會員刪除專案 (僅限 owned = true 的會員執行)
	@DeleteMapping("/deleteOwnedOrder/{memberId}/{orderId}")
	public String deleteOwnedOrder(@PathVariable Long memberId, @PathVariable Long orderId) {
		return memberOrderService.deleteOwnedOrder(memberId, orderId);
	}

   // 查詢某個會員相關的所有專案
	@GetMapping("/getOrdersByMemberid/{memberid}")
    public List<MemberOrder> getOrdersByMemberid(@PathVariable Long memberid) throws IOException {
        // 查询所有对应 memberid 的订单
        List<MemberOrder> orders = memberOrderRepository.findAllById_Memberid(memberid);
        
        return orders;
    }
	

	// 查詢某個會員相關的所有專案的關係(admin)
	@GetMapping("/getAllMemberOrdersByMemberId/{memberId}")
	public List<MemberOrder> getAllMemberOrdersByMemberId(@PathVariable Long memberId) {
		return memberOrderService.getAllMemberOrdersByMemberId(memberId);
	}

	// 根據orderid來找發行會員(order)
	@GetMapping("/getMemberIdByOrderId/{orderId}")
	public List<Member> getMemberIdByOrderID(@PathVariable Long orderId) {
		return memberOrderService.getMemberByOrderId(orderId);
	}
	// 查詢某個專案相關的所有會員
	@GetMapping("/getMembersByOrderid/{orderId}")
	public List<Member> getMembersByOrderid(@PathVariable Long orderId) {
		return memberOrderService.getMemberByOrderId(orderId);
	}

	// 新增評價(order)
	@PostMapping("/addEvaluate/{orderId}")
	public String addEvaluate(@PathVariable Long orderId, @RequestBody AddMemberOrderRequest memberOrder) {
		return memberOrderService.addEvaluate(orderId, memberOrder);
	}

	// 查詢某個會員相關的所有專案評分總和(order)
	@GetMapping("/getRank/{memberId}")
	public Double getAverageRank(@PathVariable Long memberId) {
		return memberOrderService.getAverageRankByMemberId(memberId);
	}

	// 收藏案件(order)
	@PutMapping("/collected")
	public String updatecollectedStatus(@RequestBody AddMemberOrderRequest request) {
		return memberOrderService.updatecollectedStatus(request);
	}

	// 查詢收藏案件是否為true(order)
	@GetMapping("/collected/{orderId}/{memberId}")
	public Boolean getMemberCollected(@PathVariable Long orderId, @PathVariable Long memberId) {
		return memberOrderService.getMemberCollected(orderId, memberId);
	}

	// 取消收藏 (collected設為false)(order)
	@PutMapping("/removeMemberCollected")
	public String removeMemberCollected(@RequestBody AddMemberOrderRequest request) {
		request.setCollected(false); // 將 collected 設置為 false，代表取消收藏
		return memberOrderService.removeMemberCollected(request);
	}
	
	

	// 查詢申請案件人數(order)
	@GetMapping("/wanted/people/{orderId}")
	public Long getwanted(@PathVariable Long orderId) {
		return memberOrderService.getwanted(orderId);
	}

	// 查詢會員是否已wantrd(order)
	@GetMapping("/memberWanted/{orderId}/{memberId}")
	public Boolean getMemberWanted(@PathVariable Long orderId, @PathVariable Long memberId) {
		return memberOrderService.getMemberWanted(orderId, memberId);
	}

	// 查詢所有wanted(除了已決定get)的人(order)
	@GetMapping("/allMemberWanted/{orderId}")
	public List<Member> getAllMemberWanted(@PathVariable Long orderId) {
		return memberOrderService.getAllMemberWanted(orderId);
	}

	// 決定get案件的人(order)
	@PutMapping("/getproject")
	public String getproject(@RequestBody AddMemberOrderRequest request) {
		return memberOrderService.getproject(request);
	}

	// 查詢所有決定get案件後的人(order)
	@GetMapping("/allMemberGeted/{orderId}")
	public List<Member> getAllMemberGeted(@PathVariable Long orderId) {
		return memberOrderService.getallMemberGeted(orderId);
	}

	@Autowired
	private MemberOrderRepository memberOrderRepository;

	//memberlike跳轉後端
	// 根據 memberId 和 orderId 獲取該會員對某個專案的收藏狀態（如：wanted）
	@GetMapping("/memberlike/{memberid}")
	public boolean getMemberCollecteded(@PathVariable Long memberid, @RequestParam Long orderid) {
		Optional<MemberOrder> memberOrder = memberOrderService.getOrderByMemberAndOrder(memberid, orderid);

		// 返回該會員對某個專案是否有收藏（wanted）
		return memberOrder.isPresent() && memberOrder.get().isCollected();
	}

	// 更新收藏狀態
	@PostMapping("/updateFavoriteStatus")
    public void updateFavoriteStatus(@RequestBody UpdateFavoriteRequest request) {
        memberOrderService.updateCollectedStatus(request.getMemberid(), request.getOrderid(), request.isCollected());
	}

	@GetMapping("/Evaluate/{orderid}")
	public String Evaluate(@PathVariable Long orderid, @RequestBody AddMemberOrderRequest memberOrder) {
		memberOrderService.addEvaluate(orderid, memberOrder);
		return "evaluate"; // 假設返回一個評分頁面的模板或數據
	}

	@GetMapping("/getEvaluateAndRank/{orderId}")
	public List<MemberOrder> getEvaluateAndRank(@PathVariable Long orderId) {
		return memberOrderService.getEvaluateAndRank(orderId);
	}
}
