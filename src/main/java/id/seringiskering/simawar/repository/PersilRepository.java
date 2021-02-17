package id.seringiskering.simawar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import id.seringiskering.simawar.entity.Persil;

public interface PersilRepository extends JpaRepository<Persil, String> {
	
	List<Persil> findPersilByClusterIdAndBlokIdAndBlokNumberAndBlokIdentity(String clusterId, String blokId, int blokNumber, String blokIdentity);
	
	@Query("SELECT COUNT(u.persilId) from Persil u")
    int getJumlahPersil();

}
