package tw.platform.sideProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.platform.sideProject.model.mimiTag;


@Repository
public interface mimiTagRepository extends JpaRepository<mimiTag, Long> {
	
}
