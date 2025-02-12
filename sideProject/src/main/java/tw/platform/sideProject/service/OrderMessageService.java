package tw.platform.sideProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.platform.sideProject.model.MemberOrder;
import tw.platform.sideProject.model.Order;
import tw.platform.sideProject.model.mimiMember;
import tw.platform.sideProject.repository.MemberOrderRepository;
import tw.platform.sideProject.repository.MessageRepository;
import tw.platform.sideProject.repository.OrderRepository;
import tw.platform.sideProject.repository.mimiMemberRepository;

@Service
public class OrderMessageService {
	
	@Autowired
	private MemberOrderRepository memberOrderRepository;
	@Autowired
	private MessageService messageService;
	@Autowired
	private OrderRepository orderRepository; // 需要獲取 order.name
	@Autowired
	private mimiMemberRepository memberRepository; // 需要查詢 receiver 會員信息
	@Autowired
	private MessageRepository messageRepository;
	private static final Long SENDER_ID = 1L; // 固定的 sender ID

	
	@Scheduled(fixedRate = 5000) // 每 5 秒檢查一次
	@Transactional
	public void checkAndSendMessages() {
		List<MemberOrder> orders = memberOrderRepository.findByGetprojectTrue();

		for (MemberOrder order : orders) {
			mimiMember sender = memberRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Sender not found"));
			mimiMember receiver = memberRepository.findById(order.getMember().getMemberid())
					.orElseThrow(() -> new RuntimeException("Receiver not found"));
			Order project = orderRepository.findById(order.getOrder().getOrderid())
					.orElseThrow(() -> new RuntimeException("Order not found"));

			String title = "您已成功加入 " + project.getName() + " 專案";

			// 🔍 **檢查是否已經發送過這封信**
			boolean messageExists = messageRepository.existsByReceiveridAndTitle(receiver, title);

			if (!messageExists) {
				String content = "恭喜您成功加入 " + project.getName() + " 專案";
				messageService.sendOrderMesg(sender, receiver, title, content);
			}
		}
	}

}
