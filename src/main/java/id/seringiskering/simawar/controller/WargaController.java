package id.seringiskering.simawar.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;
import id.seringiskering.simawar.service.WargaService;

@RestController
@RequestMapping(path = { "/warga" })
public class WargaController {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	private WargaService wargaService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/findFamilyMember")
	public ResponseEntity<List<ListWargaResponse>> findFamilyMember() {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<ListWargaResponse> response = wargaService.findFamilyMember(username);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}
	
	@GetMapping("/findFamilyMemberById/{id}")
	@PreAuthorize("hasAnyAuthority('warga:update')")
	public ResponseEntity<WargaResponse> findFamilyMemberById(@PathVariable("id") String id) {
		String username = jwtAuthorizationFilter.getValidUsername();
		WargaResponse response = wargaService.findFamilyMemberById(username, Long.parseLong(id));
		return new ResponseEntity<WargaResponse>(response, HttpStatus.OK);
	}
	
}
