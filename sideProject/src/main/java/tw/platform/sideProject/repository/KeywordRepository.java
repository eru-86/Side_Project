package tw.platform.sideProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.platform.sideProject.model.Keywords;

@Repository
public interface KeywordRepository extends JpaRepository<Keywords, Long> {

	// 抓取用戶輸入的關鍵詞
	Optional<Keywords> findByKeyword(String keyword);

	// 抓取回傳資料的前5筆
	List<Keywords> findTop5ByOrderByTimesDesc();

	// 抓取隨機關鍵字
	@Query(value = "SELECT * FROM Keywords ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Keywords findRandomKeyword();

	// 根據關鍵字進行模糊查詢
	@Query("SELECT k FROM Keywords k WHERE k.keyword LIKE %:keyword%")
	List<Keywords> findByKeywordSearch(@Param("keyword") String keyword);

}