package id.seringiskering.simawar.service;

import java.util.List;

import id.seringiskering.simawar.entity.UserRegister;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.request.registrasi.UserRegistrationRequest;

public interface RegisterService {
	
	void registerNewUser(UserRegistrationRequest request) throws UsernameExistException, EmailExistException; 
	List<UserRegister> findUserRegisterForApprove(String username);

}
