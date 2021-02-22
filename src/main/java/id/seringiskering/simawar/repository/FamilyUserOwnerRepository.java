package id.seringiskering.simawar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.FamilyUserOwner;
import id.seringiskering.simawar.entity.FamilyUserOwnerPK;

public interface FamilyUserOwnerRepository extends JpaRepository<FamilyUserOwner, FamilyUserOwnerPK> {
	Optional<List<FamilyUserOwner>> findByIdUserId(String userId);
}
