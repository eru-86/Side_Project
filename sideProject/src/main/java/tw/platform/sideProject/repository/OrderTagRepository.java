package tw.platform.sideProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tw.platform.sideProject.model.OrderTag;
import tw.platform.sideProject.model.OrderTagKey;

@Repository
public interface OrderTagRepository extends JpaRepository<OrderTag, OrderTagKey> {
 
    Optional<OrderTag> findById(OrderTagKey id);
    void deleteById(OrderTagKey id);
    
    // 刪除特定專案的所有標籤關聯
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderTag ot WHERE ot.id.orderId = :orderId")
    void deleteByOrderid(Long orderId);
}
