package tw.platform.sideProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tw.platform.sideProject.model.MemberTag;
import tw.platform.sideProject.model.MemberTagKey;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, MemberTagKey> {
    
    Optional<MemberTag> findById(MemberTagKey id);
    void deleteById(MemberTagKey id);
    
    // 刪除會員相關的所有標籤
    @Modifying
    @Transactional
    @Query("DELETE FROM MemberTag mt WHERE mt.id.memberId = :memberId")
    void deleteByMemberId(Long memberId);
}

