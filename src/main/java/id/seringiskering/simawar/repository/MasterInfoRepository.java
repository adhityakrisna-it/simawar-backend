package id.seringiskering.simawar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.MasterInformasi;

public interface MasterInfoRepository extends JpaRepository<MasterInformasi, String> {
	
}
