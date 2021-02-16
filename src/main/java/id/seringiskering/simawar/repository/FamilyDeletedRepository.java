package id.seringiskering.simawar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.FamilyDeleted;
import id.seringiskering.simawar.entity.FamilyDeletedPK;

public interface FamilyDeletedRepository extends JpaRepository<FamilyDeleted, FamilyDeletedPK> {

}
