package id.seringiskering.simawar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.FamilyMemberDeleted;
import id.seringiskering.simawar.entity.FamilyMemberDeletedPK;

public interface FamilyMemberDeletedRepository extends JpaRepository<FamilyMemberDeleted, FamilyMemberDeletedPK> {
	
}
