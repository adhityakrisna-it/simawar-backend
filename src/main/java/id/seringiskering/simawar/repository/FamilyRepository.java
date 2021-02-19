package id.seringiskering.simawar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import id.seringiskering.simawar.entity.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {
	
	@Query("SELECT f.id, f.blok, f.cluster "
			+ " from Family f inner join FamilyMember g on f.id=g.family.id where f.id=:id order by g.familyStatus")
	Optional<Family> findByIdorderbyfamily(Long id);
	
}
