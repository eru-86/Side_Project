package tw.platform.sideProject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tw.platform.sideProject.model.AddMemberOrderRequest;
import tw.platform.sideProject.model.AddMemberRequest;
import tw.platform.sideProject.model.AddOrderRequest;
import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.model.Order;
import tw.platform.sideProject.model.OrderTag;
import tw.platform.sideProject.model.Tag;
import tw.platform.sideProject.model.MemberOrder;
import tw.platform.sideProject.model.MemberOrderKey;
import tw.platform.sideProject.model.MemberTag;
import tw.platform.sideProject.model.MemberTagKey;
import tw.platform.sideProject.repository.MemberRepository;
import tw.platform.sideProject.repository.MemberTagRepository;
import tw.platform.sideProject.repository.OrderRepository;
import tw.platform.sideProject.repository.OrderTagRepository;
import tw.platform.sideProject.repository.TagRepository;

@RestController
@RequestMapping("/test")
public class Test {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MemberTagRepository memberTagRepository;
    
    @Autowired
    private OrderTagRepository orderTagRepository;

    // 新增會員
    @PostMapping("/addMember")
    public String addMember(@RequestBody AddMemberRequest request) {
        // 建立並儲存會員
        Member member = new Member();
        member.setAccount(request.getAccount());
        member.setPassword(request.getPassword());
        member.setEmail(request.getEmail());
        member.setName(request.getName());
        member.setIsblocked(false);
        memberRepository.save(member);

        // 處理傳入的 tagIds，建立 MemberTag 資料
        List<MemberTag> memberTags = new ArrayList<>();
        for (Integer tagId : request.getTagIds()) {
            // 查找對應的 Tag
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found for ID: " + tagId));

            // 建立 MemberTagKey 作為複合主鍵
            MemberTagKey memberTagKey = new MemberTagKey();
            memberTagKey.setMemberId(member.getMemberid());
            // memberTagKey.setTagId(tag.getTagidm());

            // 建立 MemberTag 並設置關聯
            MemberTag memberTag = new MemberTag();
            memberTag.setId(memberTagKey);
            memberTag.setMember(member);
            memberTag.setTag(tag);

            memberTags.add(memberTag);
        }

        // 儲存所有的 MemberTag
        memberTagRepository.saveAll(memberTags);

        return "會員新增成功，並建立對應的標籤關聯";
    }

    // 新增標籤
    @PostMapping("/addTag")
    public String addTag() {
        // 新增技能 (Tag)
        Tag javaTag = new Tag();
        // javaTag.setTagidm(5);
//        javaTag.setTagido(5);
        javaTag.setTagname("JavaScript");
        tagRepository.save(javaTag);

        Tag springTag = new Tag();
        // springTag.setTagidm(6);
//        springTag.setTagido(6);
        springTag.setTagname("Python");
        tagRepository.save(springTag);

        return "標籤新增成功";
    }

    // 新增專案
    @PostMapping("/addOrder")
    public String addOrder(@RequestBody AddOrderRequest request) {
        // 建立並儲存 Order
        Order order = new Order();
        order.setName(request.getName());
        order.setDeadline(request.getDeadline());
        order.setDetail(request.getDetail());
        order.setPeople(request.getPeople());
        orderRepository.save(order);

        // 建立並儲存 OrderTag
        List<OrderTag> orderTags = new ArrayList<>();
        for (Integer tagId : request.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found for ID: " + tagId));

            // OrderTagKey orderTagKey = new OrderTagKey(order.getOrderid(), tag.getTagidm());
            OrderTag orderTag = new OrderTag();
            // orderTag.setId(orderTagKey);
            orderTag.setOrder(order);
            orderTag.setTag(tag);

            orderTags.add(orderTag);
        }
        orderTagRepository.saveAll(orderTags);

        return "專案新增成功";
    }

    // 新增會員與標籤的關聯
    @PostMapping("/addMemberOrder")
    public String addMemberOrder(@RequestBody AddMemberOrderRequest request) {
    	// 查找會員
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 查找專案
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 建立關聯
        MemberOrderKey memberOrderKey = new MemberOrderKey(member.getMemberid(), order.getOrderid());
        MemberOrder memberOrder = new MemberOrder();
        memberOrder.setId(memberOrderKey);
        memberOrder.setMember(member);
        memberOrder.setOrder(order);
        memberOrder.setOwned(request.isOwned());
        memberOrder.setWanted(request.isWanted());

        // 儲存關聯
//        memberOrderService.saveMemberOrder(memberOrder);

        return "會員與專案的關聯已新增成功";
    }
}