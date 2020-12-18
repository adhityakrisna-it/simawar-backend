package id.seringiskering.simawar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.User;


public interface UserRepository extends JpaRepository<User, String> {

	User findUserByUsername(String username);
	User findUserByEmail(String email);
	User findUserByUserId(String id);
	
}
