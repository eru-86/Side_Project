package tw.platform.sideProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.platform.sideProject.model.Message;
import tw.platform.sideProject.model.mimiMember;



@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
	//查詢message的receiverid
	@Query("SELECT m FROM Message m WHERE m.receiverid.memberid = :receiverid")
	List<Message> findByReceiverid(@Param("receiverid") Long receiverid);

	Optional<Message> findByMessageid(Long messageid);
	boolean existsByReceiveridAndTitle(mimiMember receiverid, String title);
}
