package id.seringiskering.simawar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.request.registrasi.UserRegistrationRequest;
import id.seringiskering.simawar.service.RegisterService;

@RestController
@RequestMapping(path = { "/register" })
public class RegisterController {
	
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
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		// TODO Auto-generated method stub
		return new ResponseEntity<> (new HttpResponse(httpStatus.value(),httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
	}	
	

}
