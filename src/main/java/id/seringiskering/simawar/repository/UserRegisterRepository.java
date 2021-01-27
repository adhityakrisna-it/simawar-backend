package id.seringiskering.simawar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import id.seringiskering.simawar.entity.UserRegister;

public interface UserRegisterRepository extends JpaRepository<UserRegister, Long> {

	UserRegister findUserRegisterByUsernameAndRegisterStatus(String username, String registerStatus);
	UserRegister findUserRegisterByEmailAndRegisterStatus(String email, String registerStatus);
	List<UserRegister> findUserRegisterByRegisterStatusAndRtId(String registerStatus, int rtId);
	List<UserRegister> findUserRegisterByRegisterStatusAndRwId(String registerStatus, int rwId);
	List<UserRegister> findUserRegisterByRegisterStatusAndClusterId(String registerStatus, String clusterId);
	List<UserRegister> findUserRegisterByRegisterStatus(String registerStatus);

	
}
