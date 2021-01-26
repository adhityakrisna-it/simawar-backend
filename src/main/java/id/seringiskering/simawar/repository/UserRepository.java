package id.seringiskering.simawar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import id.seringiskering.simawar.entity.User;


public interface UserRepository extends JpaRepository<User, String> {

	User findUserByUsername(String username);
	User findUserByEmail(String email);
	User findUserByUserId(String id);
	
	@Query(value = "SELECT * FROM user a WHERE EXISTS "
			+ "(SELECT 1 FROM user_persil x WHERE a.user_id=x.user_id "
			+ "AND x.rt=:rt)", nativeQuery = true)
	Optional<List<User>> queryByRt(@Param("rt") String rt);
	
	@Query(value = "SELECT * FROM user a WHERE EXISTS "
			+ "(SELECT 1 FROM user_persil x WHERE a.user_id=x.user_id "
			+ "AND x.rw=:rw)", nativeQuery = true)
	Optional<List<User>> queryByRw(@Param("rw") String rw);
	
	
}
