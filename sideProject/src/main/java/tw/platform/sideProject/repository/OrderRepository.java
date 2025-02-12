package tw.platform.sideProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.platform.sideProject.model.Order;
import tw.platform.sideProject.model.yuOrder;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByName(String name);

	List<Order> findByRankGreaterThanEqual(int rank);

//	YU新增
	// 模糊查詢 name 或 intro 符合 keyword
	@Query("SELECT o FROM yuOrder o WHERE o.name LIKE %:keyword% OR o.intro LIKE %:keyword%")
	List<yuOrder> searchByKeyword(@Param("keyword") String keyword);

	// 抓取隨機專案
	@Query("SELECT o FROM yuOrder o ORDER BY FUNCTION('RAND')")
	List<yuOrder> findRandomYuOrders();

	// 抓取指定id的專案資料
	@Query("SELECT o FROM yuOrder o WHERE o.orderid = :orderid")
	List<yuOrder> findYuOrdersById(@Param("orderid") Long orderid);
}
