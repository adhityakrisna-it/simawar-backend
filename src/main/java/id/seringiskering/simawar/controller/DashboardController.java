package id.seringiskering.simawar.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.response.dashboard.CardResponse;
import id.seringiskering.simawar.response.dashboard.InfoIuranResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.service.DashboardService;
import id.seringiskering.simawar.service.WargaService;

@RestController
@RequestMapping(path = { "/dashboard" })
public class DashboardController {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/findCardResponse")
	public ResponseEntity<CardResponse> findCardResponse() {
		String username = jwtAuthorizationFilter.getValidUsername();
		CardResponse response = dashboardService.findCardResponse(username);
		return new ResponseEntity<CardResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/findRekapIuranResponse")
	public ResponseEntity<InfoIuranResponse> findRekapIuranResponse() {
		String username = jwtAuthorizationFilter.getValidUsername();
		InfoIuranResponse response = dashboardService.findRekapIuranResponse(username);
		return new ResponseEntity<InfoIuranResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/findFamily")
	public ResponseEntity<List<ListKeluargaResponse>> findFamily() {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<ListKeluargaResponse> response = dashboardService.findAllKeluarga(username);
		return new ResponseEntity<List<ListKeluargaResponse>>(response, HttpStatus.OK);
	}	
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		// TODO Auto-generated method stub
		return new ResponseEntity<> (new HttpResponse(httpStatus.value(),httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
	}
}
