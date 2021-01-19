package id.seringiskering.simawar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.MasterRole;

public interface MasterRoleRepository extends JpaRepository<MasterRole, String> {
	List<MasterRole> findByRoleIdNotIn(List<String> listRole);
}
