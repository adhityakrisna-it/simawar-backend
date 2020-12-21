package id.seringiskering.simawar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import id.seringiskering.simawar.entity.MasterBlok;
import id.seringiskering.simawar.entity.MasterBlokPK;

public interface MasterBlokRepository extends JpaRepository<MasterBlok, MasterBlokPK> {
	
	@Query(value = "select * FROM master_blok u WHERE u.cluster_id = :clusterid",nativeQuery = true)
	List<MasterBlok> findMasterBlokByClusterIdList(@Param("clusterid") String clusterId);
}
