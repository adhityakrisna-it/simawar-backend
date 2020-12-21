package id.seringiskering.simawar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.UserRegister;

public interface UserRegisterRepository extends JpaRepository<UserRegister, Long> {

	UserRegister findUserRegisterByUsernameAndRegisterStatus(String username, String registerStatus);
	UserRegister findUserRegisterByEmailAndRegisterStatus(String email, String registerStatus);

	
}
