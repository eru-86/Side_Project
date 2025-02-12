package tw.platform.sideProject.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.platform.sideProject.model.AddOrderRequest;
import tw.platform.sideProject.model.Order;
import tw.platform.sideProject.model.OrderTag;
import tw.platform.sideProject.model.OrderTagKey;
import tw.platform.sideProject.model.Tag;
import tw.platform.sideProject.model.yuOrder;
import tw.platform.sideProject.repository.MemberOrderRepository;
import tw.platform.sideProject.repository.OrderRepository;
import tw.platform.sideProject.repository.OrderTagRepository;
import tw.platform.sideProject.repository.TagRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private OrderTagRepository orderTagRepository;
	
	@Autowired
	private MemberOrderRepository memberOrderRepository;

	// 新增專案
	public Map<String, Object> addOrder(AddOrderRequest request) {
		Map<String, Object> response = new HashMap<>();
		// 建立並儲存 Order
		Order order = new Order();
		order.setName(request.getName());
		order.setIntro(request.getIntro());
		order.setDeadline(request.getDeadline());
		order.setDetail(request.getDetail());
		order.setPicurl(request.getPicurl());
		order.setLocation(request.getLocation());
		order.setPeople(request.getPeople());
		order.setBudget(request.getBudget());
		order.setUpload(request.getUpload());
		order.setNewdate(request.getNewdate());
		order.setCategory(request.getCategory());
		orderRepository.save(order);

		// 建立並儲存 OrderTag
		List<OrderTag> orderTags = new ArrayList<>();
		for (Integer tagId : request.getTagIds()) {
			Tag tag = tagRepository.findById(tagId)
					.orElseThrow(() -> new RuntimeException("Tag not found for ID: " + tagId));

			OrderTagKey orderTagKey = new OrderTagKey(order.getOrderid(), tag.getTagid());
			OrderTag orderTag = new OrderTag();
			orderTag.setId(orderTagKey);
			orderTag.setOrder(order);
			orderTag.setTag(tag);

			orderTags.add(orderTag);
		}
		orderTagRepository.saveAll(orderTags);
		System.out.println(order.getOrderid());

		response.put("orderId", order.getOrderid());
		return response;
	}

	// 查詢所有專案
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	// 根據 ID 查詢專案
	public Optional<Order> getOrderById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	// 更新專案資料
	public Order updateOrder(Long orderId,AddOrderRequest request) {

		return orderRepository.findById(orderId).map(existingOrder -> {
			// 僅更新非空欄位
			if (request.getName() != null) {
	            existingOrder.setName(request.getName());
	        }
	        if (request.getIntro() != null) {
	            existingOrder.setIntro(request.getIntro());
	        }
	        if (request.getDeadline() != null) {
	            existingOrder.setDeadline(request.getDeadline());
	        }
	        if (request.getDetail() != null) {
	            existingOrder.setDetail(request.getDetail());
	        }
	        if (request.getPicurl() != null) {
	            existingOrder.setPicurl(request.getPicurl());
	        }
	        if (request.getLocation() != null) {
	            existingOrder.setLocation(request.getLocation());
	        }
	        if (request.getRank() != null) {
	            existingOrder.setRank(request.getRank());
	        }
	        if (request.getPeople() != -1) {
	            existingOrder.setPeople(request.getPeople());
	        }
	        if (request.getBudget() != null) {
	            existingOrder.setBudget(request.getBudget());
	        }
	        if (request.getNewdate() != null) {
	            existingOrder.setNewdate(request.getNewdate());
	        }
	        if (request.getCategory() != null) {
	            existingOrder.setCategory(request.getCategory());
	        }


			if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
	            // 刪除舊的標籤
	            orderTagRepository.deleteByOrderid(orderId);

	            // 根據傳來的 tagIds 更新標籤
	            List<OrderTag> orderTags = new ArrayList<>();
	            for (Integer tagId : request.getTagIds()) {
	    			Tag tag = tagRepository.findById(tagId)
	    					.orElseThrow(() -> new RuntimeException("Tag not found for ID: " + tagId));

	                OrderTagKey orderTagKey = new OrderTagKey(orderId, tag.getTagid());
	                OrderTag orderTag = new OrderTag();
	                orderTag.setId(orderTagKey);
	                orderTag.setOrder(existingOrder);  // 設定訂單
	                orderTag.setTag(tag);  // 設定標籤

	                orderTags.add(orderTag);
	            }
	            // 儲存新標籤
	            orderTagRepository.saveAll(orderTags);
	        }
			
			
			// 儲存更新後的資料
			return orderRepository.save(existingOrder);
		}).orElseThrow(() -> new RuntimeException("專案 ID 不存在: " + orderId));
	}

	// 刪除專案
	public void deleteOrder(Long orderId) {
		// 刪除該專案的所有 OrderTag
		orderTagRepository.deleteByOrderid(orderId);

		// 刪除專案
		orderRepository.deleteById(orderId);
	}

	public Integer addRank(Long orderId, AddOrderRequest rank) {
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isPresent()) {
			Order order = orderOptional.get();
			order.setRank(rank.getRank()); // 更新 rank 分數
			orderRepository.save(order); // 保存更新後的訂單
			return order.getRank(); // 回傳新的 rank
		}
		throw new RuntimeException("專案 ID 不存在: " + orderId);
	}

//	YU新增
//	抓取隨機5個專案
	public List<yuOrder> getRandomYuOrders() {
		List<yuOrder> allOrders = orderRepository.findRandomYuOrders();
		// 只取前5個
		return allOrders.stream().limit(5).collect(Collectors.toList());
	}

	public List<yuOrder> getAllYuOrders() {
		return orderRepository.findRandomYuOrders();
	}

	//抓取所有專案申請人數
	public List<Object[]> getWantedCountByOrderId() {
		return memberOrderRepository.findWantedCountByOrderId();
	}
	//抓取所有專案收藏人數
//	public List<Object[]> getAllcaseCollectedCount() {
//		return memberOrderRepository.findCollectedCountByOrderId();
//	}
	//抓取指定專案收藏人數
	public Long getCollectedCountByOrderId(Long orderId) {
	    List<Object[]> result = memberOrderRepository.findCollectedCountByOrderId(orderId);
	    if (result.isEmpty()) {
	        return 0L;
	    }
	    return (Long) result.get(0)[0];
	}
	
	public List<yuOrder> getyuOrderById(Long orderid){
		return orderRepository.findYuOrdersById(orderid);
	}

}
