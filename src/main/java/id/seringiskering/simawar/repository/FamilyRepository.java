package id.seringiskering.simawar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import id.seringiskering.simawar.entity.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {
	
	@Query("SELECT f.id, f.blok, f.cluster "
			+ " from Family f inner join FamilyMember g on f.id=g.family.id where f.id=:id order by g.familyStatus")
	Optional<Family> findByIdorderbyfamily(Long id);
	List<Family> findAllByOrderByClusterAscBlokAscNomorAsc();
	
	@Query("SELECT a from Family a left join FamilyUserOwner b on a.id=b.id.id where b.id.id is NULL ")
	Optional<List<Family>> findByLeftJoinFamilyUser();
	
}
