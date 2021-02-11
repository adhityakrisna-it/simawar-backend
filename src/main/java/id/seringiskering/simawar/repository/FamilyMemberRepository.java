package id.seringiskering.simawar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import id.seringiskering.simawar.entity.FamilyMember;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long>, JpaSpecificationExecutor<FamilyMember> {

	Optional<List<FamilyMember>> findByFamilyId(Long id);
	
}
