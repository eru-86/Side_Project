package tw.platform.sideProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.model.MemberOrder;
import tw.platform.sideProject.model.MemberOrderKey;
import tw.platform.sideProject.model.Order;

@Repository
public interface MemberOrderRepository extends JpaRepository<MemberOrder, MemberOrderKey> {
	// 使用 member.memberid 和 order.orderid 進行查詢
	Optional<MemberOrder> findByMember_memberidAndOrder_orderid(Long memberId, Long orderId);

	@Modifying
	@Query("DELETE FROM MemberOrder mo WHERE mo.member.id = :memberid")
	void deleteAllByMemberid(@Param("memberid") Long memberid);

	@Modifying
	@Query("DELETE FROM MemberOrder mo WHERE mo.order.id = :orderid")
	void deleteAllByOrderid(@Param("orderid") Long orderid);

	// 刪除所有 wanted 和 owned 都為 false 的紀錄
	@Modifying
	@Transactional
	@Query("DELETE FROM MemberOrder mo WHERE mo.wanted = false AND mo.owned = false")
	void deleteAllByWantedFalseAndOwnedFalse();

	// 查找與某個會員相關的所有專案
	@Query("SELECT mo.order FROM MemberOrder mo WHERE mo.member.memberid = :memberId")
	List<Order> findAllOrdersByMemberid(Long memberId);

	// 查找與某個專案相關的所有會員
	@Query("SELECT mo.member FROM MemberOrder mo WHERE mo.order.orderid = :orderId")
	List<Member> findAllMembersByOrderid(Long orderId);

	// 查詢某個會員相關的所有專案的關係(admin)
	@Query("SELECT mo FROM MemberOrder mo WHERE mo.member.memberid = :memberId")
	List<MemberOrder> findAllByMemberid(@Param("memberId") Long memberId);

	List<MemberOrder> findByOrder_orderid(Long orderId);

	// 查找會員的所有專案評分的平均(order)
	@Query("SELECT o.rank FROM MemberOrder mo JOIN mo.order o WHERE mo.member.memberid = :memberId AND mo.owned = true")
	List<Integer> findRanksByMemberIdAndOwnedTrue(@Param("memberId") Long memberId);

	// 查發布專案的會員(order)
	@Query("SELECT mo.member FROM MemberOrder mo WHERE mo.order.id = :orderId AND mo.owned = true")
	List<Member> findMemberByOrderid(@Param("orderId") Long orderId);

	// 查詢該專案的申請人數(order)
	@Query("SELECT COUNT(mo) FROM MemberOrder mo WHERE mo.order.id = :orderId AND mo.wanted = true")
	Long countWantedByOrderId(@Param("orderId") Long orderId);

	// 查詢會員是否已wantrd(order)
	@Query("SELECT mo.wanted FROM MemberOrder mo WHERE mo.id.orderid = :orderId AND mo.id.memberid = :memberId")
	Boolean getMemberWanted(@Param("orderId") Long orderId, @Param("memberId") Long memberId);

	// 查詢該專案所有Wanted(除了已決定get)的人(order)
	@Query("SELECT mo.member FROM MemberOrder mo WHERE mo.order.id = :orderId AND mo.wanted = true AND mo.getproject = false")
	List<Member> getAllMemberWanted(@Param("orderId") Long orderId);

	// 查詢收藏案件是否為true(order)
	@Query("SELECT mo.collected FROM MemberOrder mo WHERE mo.id.orderid = :orderId AND mo.id.memberid = :memberId")
	Boolean getMemberCollected(@Param("orderId") Long orderId, @Param("memberId") Long memberId);

	// 更新收藏狀態(order)
	@Modifying
	@Transactional
	@Query("UPDATE MemberOrder mo SET mo.collected = :collected WHERE mo.member.id = :memberId AND mo.order.id = :orderId")
	void updateCollectedStatus(@Param("collected") boolean collected, @Param("memberId") Long memberId,
			@Param("orderId") Long orderId);

	// 更新getproject狀態(order)
	@Modifying
	@Query("UPDATE MemberOrder mo SET mo.getproject = :getproject WHERE mo.member.id = :memberId AND mo.order.id = :orderId")
	void getproject(@Param("getproject") boolean getproject, @Param("memberId") Long memberId,
			@Param("orderId") Long orderId);

	// // 查詢所有決定get案件後的人(order)
	@Modifying
	@Query("SELECT mo.member FROM MemberOrder mo WHERE mo.id.orderid = :orderId AND mo.getproject = true ")
	List<Member> getallMemberGeted(@Param("orderId") Long orderId);

	// (member)
	List<MemberOrder> findAllById_Memberid(Long memberid); // 由於使用了複合主鍵，所以需要用 `Id_` 來指明 `MemberOrderKey` 的欄位

	List<MemberOrder> findAllById_Orderid(Long orderid); // 同理，查詢基於 `orderId`

	// 查詢會員收藏的所有專案
	List<MemberOrder> findByMember_MemberidAndCollectedTrue(Long memberId);

	// 找member跟order的wanted
	List<MemberOrder> findWantedByMember_memberidAndOrder_orderid(Long memberid, Long orderid);

	// yu新增
	// 統計每個專案的申請人數
	@Query("SELECT yo, COALESCE(COUNT(mo.wanted), 0) AS wanted_count " + "FROM yuOrder yo LEFT JOIN yo.memberOrders mo "
			+ "ON yo.orderid = mo.id.orderid AND mo.wanted = true " + "WHERE yo.deadline>CURRENT_DATE " + "GROUP BY yo "
			+ "ORDER BY wanted_count DESC")
	List<Object[]> findWantedCountByOrderId();

	// 統計每個專案的收藏人數
	@Query("SELECT yo, COALESCE(COUNT(mo.wanted), 0) AS collected_count "
			+ "FROM yuOrder yo LEFT JOIN yo.memberOrders mo " + "ON yo.orderid = mo.id.orderid AND mo.collected = true "
			+ "WHERE yo.deadline>CURRENT_DATE " + "GROUP BY yo " + "ORDER BY collected_count DESC")
	List<Object[]> findCollectedCountByOrder();

	// 查詢指定專案id的收藏人數
	@Query("SELECT COALESCE(COUNT(mo.wanted), 0) AS collected_count " + "FROM yuOrder yo LEFT JOIN yo.memberOrders mo "
			+ "ON yo.orderid = mo.id.orderid AND mo.collected = true "
			+ "WHERE yo.deadline > CURRENT_DATE AND yo.orderid = :orderId " + "GROUP BY yo")
	List<Object[]> findCollectedCountByOrderId(Long orderId);

	// 站內信專用
	List<MemberOrder> findByGetprojectTrue(); // 查找 getproject = true 的所有記錄
	
	@Modifying
    @Transactional
    @Query("UPDATE MemberOrder mo SET mo.collected = :collected WHERE mo.member.memberid = :memberid AND mo.order.orderid = :orderid")
    void updateCollectedStatus(@Param("memberid") Long memberid, @Param("orderid") Long orderid, @Param("collected") boolean collected);
	
	// 新增評價
	List<MemberOrder> findByOrder_orderidAndGetprojectAndMember_memberid(Long orderId, Boolean getProject,
			Long memberId);

	@Query("SELECT mo FROM MemberOrder mo WHERE mo.order.id = :orderId AND mo.getproject = true")
	List<MemberOrder> getEvaluateAndRank(@Param("orderId") Long orderId);
}