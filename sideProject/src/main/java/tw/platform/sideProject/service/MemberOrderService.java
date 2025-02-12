package tw.platform.sideProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.platform.sideProject.model.AddMemberOrderRequest;
import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.model.MemberOrder;
import tw.platform.sideProject.model.MemberOrderKey;
import tw.platform.sideProject.model.Order;
import tw.platform.sideProject.repository.MemberOrderRepository;
import tw.platform.sideProject.repository.MemberRepository;
import tw.platform.sideProject.repository.OrderRepository;

@Service
public class MemberOrderService {

	@Autowired
	private MemberOrderRepository memberOrderRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OrderRepository orderRepository;

	// 新增會員創建專案的關聯 (owned設為true)
	public String addOwnedOrder(AddMemberOrderRequest request) {

		// 查找會員
		Member member = memberRepository.findById(request.getMemberId())
				.orElseThrow(() -> new RuntimeException("Member not found"));

		// 查找專案
		Order order = orderRepository.findById(request.getOrderId())
				.orElseThrow(() -> new RuntimeException("Order not found"));

		MemberOrder memberOrder = new MemberOrder();
		MemberOrderKey memberOrderKey = new MemberOrderKey();
		memberOrder.setId(memberOrderKey); // 初始化 ID
		memberOrder.getId().setMemberid(request.getMemberId());
		memberOrder.getId().setOrderid(request.getOrderId());
		memberOrder.setMember(member); // 設置 member 對象
		memberOrder.setOrder(order); // 設置 order 對象
		memberOrder.setOwned(true);
		memberOrder.setWanted(false);
		memberOrder.setCollected(false);
		memberOrderRepository.save(memberOrder);
		return "會員創建專案關聯已新增";
	}

	// 新增會員對專案有興趣的關聯 (wanted設為true)
	public String addWantedOrder(AddMemberOrderRequest request) {

		// 查找會員
		Member member = memberRepository.findById(request.getMemberId())
				.orElseThrow(() -> new RuntimeException("Member not found"));

		// 查找專案
		Order order = orderRepository.findById(request.getOrderId())
				.orElseThrow(() -> new RuntimeException("Order not found"));

		// 查詢是否已有相同的關聯
		Optional<MemberOrder> existingMemberOrder = memberOrderRepository
				.findByMember_memberidAndOrder_orderid(request.getMemberId(), request.getOrderId());

		if (existingMemberOrder.isPresent()) {
			// 如果已經存在這條關聯，則不覆蓋 collected 欄位的值
			MemberOrder memberOrder = existingMemberOrder.get();

			// 更新 wanted 為 true，保留 collected 的原值
			memberOrder.setWanted(true);

			// 保存更新後的資料
			memberOrderRepository.save(memberOrder);
			return "會員對專案有興趣的關聯已更新，保留原有的 collected 狀態";
		} else {
			// 如果該關聯不存在，則新增一條資料
			MemberOrder memberOrder = new MemberOrder();
			MemberOrderKey memberOrderKey = new MemberOrderKey();
			memberOrder.setId(memberOrderKey); // 初始化 ID
			memberOrder.getId().setMemberid(request.getMemberId());
			memberOrder.getId().setOrderid(request.getOrderId());
			memberOrder.setMember(member); // 設置 member 對象
			memberOrder.setOrder(order); // 設置 order 對象
			memberOrder.setOwned(false);
			memberOrder.setWanted(true);
			memberOrder.setCollected(false);

			memberOrderRepository.save(memberOrder);
			return "會員對專案有興趣的關聯已新增";
		}
	}

	// 移除會員對專案有興趣的關聯 (wanted設為false)
	@Transactional
	public String removeWantedOrder(Long memberId, Long orderId) {
		// 查找指定會員和專案的關聯
		MemberOrder memberOrder = memberOrderRepository.findByMember_memberidAndOrder_orderid(memberId, orderId)
				.orElseThrow(() -> new RuntimeException("找不到該會員與專案的關聯"));

		// 如果目前該關聯已經不是 'wanted'，則不進行任何操作
		if (!memberOrder.getWanted()) {
			return "該會員對專案的關聯已經為非 wanted";
		}

		// 更新 wanted 為 false
		memberOrder.setWanted(false);
		memberOrderRepository.save(memberOrder);

		// 檢查並刪除所有 wanted 和 owned 都為 false 的資料
		memberOrderRepository.deleteAllByWantedFalseAndOwnedFalse();

		return "會員對專案的 wanted 關聯已移除，並刪除所有 wanted 和 owned 都為 false 的資料";
	}

	// 會員刪除專案 (僅限 owned = true 的會員執行)
	@Transactional
	public String deleteOwnedOrder(Long memberId, Long orderId) {
		MemberOrder memberOrder = memberOrderRepository.findByMember_memberidAndOrder_orderid(memberId, orderId)
				.orElseThrow(() -> new RuntimeException("找不到該會員與專案的關聯"));
		if (!memberOrder.getOwned()) {
			return "只有創建該專案的會員才能刪除專案";
		}
		memberOrderRepository.deleteAllByOrderid(orderId);
		return "專案及所有相關關聯已刪除";
	}

	// 查找某個會員相關的所有專案
	public List<Order> getOrdersByMemberId(Long memberId) {
		return memberOrderRepository.findAllOrdersByMemberid(memberId);
	}

	// 查找某個專案相關的所有會員
	public List<Member> getMembersByOrderId(Long orderId) {
		return memberOrderRepository.findAllMembersByOrderid(orderId);
	}

	// 查詢某個會員相關的所有專案的關係(admin)
	public List<MemberOrder> getAllMemberOrdersByMemberId(Long memberId) {
		return memberOrderRepository.findAllByMemberid(memberId);
	}

	// 新增評價(order)
	public String addEvaluate(Long orderId, AddMemberOrderRequest request) {
		// 查找指定訂單號碼，且 getProject 為 true，並且指定 memberId 的 MemberOrder
		List<MemberOrder> memberOrders = memberOrderRepository
				.findByOrder_orderidAndGetprojectAndMember_memberid(orderId, Boolean.TRUE, request.getMemberId());

		if (memberOrders.isEmpty()) {
			return "找不到對應的訂單或指定的 memberId，無法新增評價";
		}

		MemberOrder memberOrder = memberOrders.get(0); // 取得符合條件的第一個紀錄

		// 設定評價和評分
		memberOrder.setEvaluate(request.getEvaluate());
		memberOrder.setRank(request.getRank());

		// 儲存評價
		memberOrderRepository.save(memberOrder);

		return "評價已成功新增";
	}

	// 查找某個會員相關的所有專案平均評分(order)
	public Double getAverageRankByMemberId(Long memberId) {
		// 查找該會員所有擁有的專案的 rank 分數
		List<Integer> ranks = memberOrderRepository.findRanksByMemberIdAndOwnedTrue(memberId);

		if (ranks.isEmpty()) {
			// 如果沒有找到相關專案，則返回 null 或者其他預設值
			return 0.0;
		}

		// 計算平均 rank 分數，將 null 視為 0
		double sum = 0;
		int count = 0;

		for (Integer rank : ranks) {
			sum += (rank != null ? rank : 0); // 使用 0 代替 null
			count++;
		}

		return sum / count; // 返回所有 rank 的平均分數
	}

	// 尋找某個專案的發行會員(order)
	public List<Member> getMemberByOrderId(Long orderId) {
		return memberOrderRepository.findMemberByOrderid(orderId);
	}

	// 收藏專案(會員對專案的collected的狀態為true)(order)
	@Transactional
	public String updatecollectedStatus(AddMemberOrderRequest request) {
		// 檢查 MemberOrder 是否已存在
		Optional<MemberOrder> existingMemberOrder = memberOrderRepository
				.findByMember_memberidAndOrder_orderid(request.getMemberId(), request.getOrderId());

		// 如果找不到，則新增一條 MemberOrder 記錄
		if (!existingMemberOrder.isPresent()) {
			// 查找會員和專案
			Member member = memberRepository.findById(request.getMemberId())
					.orElseThrow(() -> new RuntimeException("Member not found"));
			Order order = orderRepository.findById(request.getOrderId())
					.orElseThrow(() -> new RuntimeException("Order not found"));

			// 新增一條 MemberOrder 記錄並設置 collected 屬性
			MemberOrder newMemberOrder = new MemberOrder();
			MemberOrderKey memberOrderKey = new MemberOrderKey();
			newMemberOrder.setId(memberOrderKey); // 初始化 ID
			newMemberOrder.getId().setMemberid(request.getMemberId());
			newMemberOrder.getId().setOrderid(request.getOrderId());
			newMemberOrder.setMember(member);
			newMemberOrder.setOrder(order);
			newMemberOrder.setCollected(request.isCollected()); // 設置 collected 狀態
			newMemberOrder.setOwned(false);// 設置 owned 預設值為 false
			newMemberOrder.setWanted(false); // 設置 owned 預設值為 false

			// 保存新紀錄
			memberOrderRepository.save(newMemberOrder);
			return "新增會員對專案的收藏狀態";
		} else {
			// 如果已經存在資料，就直接更新 collected 屬性
			memberOrderRepository.updateCollectedStatus(request.isCollected(), request.getMemberId(),
					request.getOrderId());
			return "會員的 collected 狀態已更新";
		}
	}

	// 取消收藏(collected設為false)(order)
	@Transactional
	public String removeMemberCollected(AddMemberOrderRequest request) {

		memberOrderRepository.updateCollectedStatus(request.isCollected(), request.getMemberId(), request.getOrderId());
		return "會員的 collected 狀態已更新";

	}

	// 尋找某個專案的發行會員(order)
	public Long getwanted(Long orderId) {
		return memberOrderRepository.countWantedByOrderId(orderId);
	}

	// 查詢會員是否已wantrd(order)
	public Boolean getMemberWanted(Long orderId, Long memberId) {
		Boolean wanted = memberOrderRepository.getMemberWanted(orderId, memberId);
		if (wanted == null) {
			return false;
		}
		return wanted;
	}

	// 查詢所有wanted(除了已決定get)的人(order)
	public List<Member> getAllMemberWanted(Long orderId) {
		return memberOrderRepository.getAllMemberWanted(orderId);
	}

	// 查詢收藏案件是否為true(order)
	public Boolean getMemberCollected(Long orderId, Long memberId) {
		Boolean Collected = memberOrderRepository.getMemberCollected(orderId, memberId);
		if (Collected == null) {
			return false;
		}
		return Collected;
	}

	// 查詢所有決定get案件後的人(order)
	public List<Member> getallMemberGeted(Long orderId) {
		return memberOrderRepository.getallMemberGeted(orderId);
	}

	// 決定get案件的人(order)
	@Transactional
	public String getproject(AddMemberOrderRequest request) {
		memberOrderRepository.getproject(request.isGetproject(), request.getMemberId(), request.getOrderId());
		return "會員的 getproject 狀態已更新";
	}

	// 根据 memberId 获取该会员的所有订单(member)
	public List<MemberOrder> getAllOrdersByMemberid(Long memberid) {
		return memberOrderRepository.findAllById_Memberid(memberid); // 查詢方法使用复合主鍵
	}

	// 根据 memberId 和 orderId 查找特定的订单(member)
	public Optional<MemberOrder> getOrderByMemberAndOrder(Long memberId, Long orderId) {
		return memberOrderRepository.findByMember_memberidAndOrder_orderid(memberId, orderId);
	}

	// 更新會員對某個專案的收藏狀態(member)
	public void updateCollectedStatus(Long memberid, Long orderid, boolean collected) {
		// TODO Auto-generated method stub
		memberOrderRepository.updateCollectedStatus(memberid, orderid, collected);

	}
	// 根據orderId列出找出評價和評分(order)
	public List<MemberOrder> getEvaluateAndRank(Long orderId) {
		return memberOrderRepository.getEvaluateAndRank(orderId);
	}
}