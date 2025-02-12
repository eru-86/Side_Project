package tw.platform.sideProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tw.platform.sideProject.model.Member;
import tw.platform.sideProject.model.yuMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	/**
	 * 根據帳號查詢會員資料
	 *
	 * @param account 會員帳號
	 * @return 會員清單
	 */
	List<Member> findByAccount(String account);

	/**
	 * 根據封鎖狀態查詢會員
	 *
	 * @param isBlocked 是否被封鎖
	 * @return 會員清單
	 */
	List<Member> findByIsblocked(Boolean isBlocked);

	/**
	 * 根據 Email 查詢會員
	 *
	 * @param email 會員的 Email
	 * @return Optional<Member>
	 */
	Optional<Member> findByEmail(String email);

	/**
	 * 檢查 Email 是否存在
	 *
	 * @param email 會員的 Email
	 * @return 是否存在
	 */
	boolean existsByEmail(String email);

	/**
	 * 檢查帳號是否存在
	 *
	 * @param account 會員的帳號
	 * @return 是否存在
	 */
	boolean existsByAccount(String account);

	/**
	 * 根據名稱和封鎖狀態查詢會員
	 *
	 * @param name      會員名稱
	 * @param isBlocked 是否被封鎖
	 * @return 會員清單
	 */
	List<Member> findByNameAndIsblocked(String name, Boolean isBlocked);

//    YU新增
	@Query("SELECT m FROM yuMember m ORDER BY FUNCTION('RAND')")
	List<yuMember> findRandomYuMembers();

	@Query("SELECT m FROM yuMember m WHERE m.memberid = :memberId")
	List<yuMember> findYuMemberByMemberId(Long memberId);
}
