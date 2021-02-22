package id.seringiskering.simawar.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.entity.UserRegister;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.UnauthorizedException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.request.registrasi.UserApprovalRequest;
import id.seringiskering.simawar.request.registrasi.UserRegistrationRequest;
import id.seringiskering.simawar.response.register.UserRegisterResponse;
import id.seringiskering.simawar.service.RegisterService;

@RestController
@RequestMapping(path = { "/register" })
public class RegisterController {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private RegisterService registerService;
	private JwtAuthorizationFilter jwtAuthorizationFilter;

	@Autowired
	public RegisterController(RegisterService registerService, JwtAuthorizationFilter jwtAuthorizationFilter) {
		this.registerService = registerService;
		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
	}
	
	@PostMapping("/registerNewUser")
	public ResponseEntity<HttpResponse> registerNewUser(@RequestBody UserRegistrationRequest request) throws UsernameExistException, EmailExistException {
		registerService.registerNewUser(request);
		return response(HttpStatus.OK, "Data registrasi sudah tersimpan");
	}
	
	@PostMapping("/approveUserRegister")
	@PreAuthorize("hasAnyAuthority('user:approval')") 
	public ResponseEntity<HttpResponse> approveUserRegister(@RequestBody UserApprovalRequest request) throws NumberFormatException, JsonProcessingException, UnauthorizedException, DataNotFoundException {
		String username = jwtAuthorizationFilter.getValidUsername();
		registerService.approveUserRegister(
											username, 
											request.getId(), 
											request.getRole(), 
											request.getClusterId(), 
											request.getBlokId(), 
											request.getBlokNumber(),
											request.getBlokIdentity(),
											request.getDataRW(),
											request.getDataRT(),
											request.getRw(),
											request.getRt(),
											request.getFamilyId()
											);
		return response(HttpStatus.OK, "Registrasi sudah disetujui");
	}

	@PostMapping("/disapproveUserRegister/{id}")
	@PreAuthorize("hasAnyAuthority('user:approval')") 
	public ResponseEntity<HttpResponse> disapproveUserRegister(@PathVariable("id") String id) 
			throws NumberFormatException, JsonProcessingException, UnauthorizedException, DataNotFoundException {
		String username = jwtAuthorizationFilter.getValidUsername();
		registerService.disapproveUserRegister(username, Long.parseLong(id));
		return response(HttpStatus.OK, "Registrasi sudah ditolak");
	}
	
	@GetMapping("/findUserRegisterStatusEntri")
	@PreAuthorize("hasAnyAuthority('user:approval')") 
	public ResponseEntity<List<UserRegisterResponse>> findUserRegisterStatusEntri() throws JsonProcessingException {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<UserRegisterResponse> userRegister = registerService.findUserRegisterForApproval(username);
		
		return new ResponseEntity<> (userRegister, HttpStatus.OK);
		
		
	}
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		// TODO Auto-generated method stub
		return new ResponseEntity<> (new HttpResponse(httpStatus.value(),httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
	}	
	

}
