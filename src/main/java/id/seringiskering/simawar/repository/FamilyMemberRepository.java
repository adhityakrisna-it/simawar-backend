package id.seringiskering.simawar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.FamilyMember;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
	
}
