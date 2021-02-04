package id.seringiskering.simawar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import id.seringiskering.simawar.entity.FamilyMember;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long>, JpaSpecificationExecutor<FamilyMember> {
	
}
