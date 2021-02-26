package id.seringiskering.simawar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.FamilyMemberUserOwner;
import id.seringiskering.simawar.entity.FamilyMemberUserOwnerPK;
import id.seringiskering.simawar.entity.FamilyUserOwner;

public interface FamilyMemberUserOwnerRepository extends JpaRepository<FamilyMemberUserOwner, FamilyMemberUserOwnerPK> {
	Optional<List<FamilyMemberUserOwner>> findByIdUserId(String userId);
}
