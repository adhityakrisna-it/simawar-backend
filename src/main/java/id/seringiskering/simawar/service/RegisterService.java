package id.seringiskering.simawar.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import id.seringiskering.simawar.entity.UserRegister;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.UnauthorizedException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.request.registrasi.UserRegistrationRequest;

public interface RegisterService {
	
	void registerNewUser(UserRegistrationRequest request) throws UsernameExistException, EmailExistException; 
	List<UserRegister> findUserRegisterForApprove(String username) throws  JsonProcessingException;
	void approveUserRegister(String username, Long id, String role) throws JsonProcessingException, UnauthorizedException, DataNotFoundException;

}
