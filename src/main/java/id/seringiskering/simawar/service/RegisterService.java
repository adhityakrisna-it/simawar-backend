package id.seringiskering.simawar.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import id.seringiskering.simawar.entity.UserRegister;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.UnauthorizedException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.request.registrasi.UserRegistrationRequest;
import id.seringiskering.simawar.response.register.UserRegisterResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;

public interface RegisterService {
	
	void registerNewUser(UserRegistrationRequest request) throws UsernameExistException, EmailExistException; 
	List<UserRegister> findUserRegisterForApprove(String username) throws  JsonProcessingException;
	List<UserRegisterResponse> findUserRegisterForApproval(String username) throws  JsonProcessingException;
	void approveUserRegister(
			 				String username, 
			 				Long id, 
			 				String role, 
			 				String cluster, 
			 				String blok, 
			 				String nomor, 
			 				String nomorTambahan,
			 				String dataRW,
			 				String dataRT,
			 				String rw,
			 				String rt,
			 				String familyId
			 				) throws JsonProcessingException, UnauthorizedException, DataNotFoundException;
	
	void disapproveUserRegister(String username, Long id) throws JsonProcessingException, UnauthorizedException, DataNotFoundException;
	List<ListKeluargaResponse> findFamilyByUser(String username);
	
}
