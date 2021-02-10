package id.seringiskering.simawar.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.entity.Persil;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.function.StringManipulation;
import id.seringiskering.simawar.response.persil.ListPersilResponse;
import id.seringiskering.simawar.service.PersilService;

@RestController
@RequestMapping(path = { "/persil" })
public class PersilController {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	
	@Autowired
	private PersilService persilService;
	
	@GetMapping("/findMasterPersil")
	public ResponseEntity<List<ListPersilResponse>> findMasterPersil() {
		String username = jwtAuthorizationFilter.getValidUsername();
		
		List<ListPersilResponse> response = new ArrayList<ListPersilResponse>();
		List<Persil> persil = persilService.findPersilByUsername(username);
		
		for (Persil item: persil) {
			ListPersilResponse list = new ListPersilResponse();
			list.setPersilId(item.getPersilId());
			String address = item.getClusterId().toUpperCase() + " " + 
							item.getBlokId().toUpperCase() + " " + 
							String.valueOf(item.getBlokNumber()) + StringManipulation.isNull(item.getBlokIdentity(), ""); 
			list.setPersilName(address);
			response.add(list);
		}
		
		return new ResponseEntity<List<ListPersilResponse>>(response, HttpStatus.OK);
	}
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		// TODO Auto-generated method stub
		return new ResponseEntity<> (new HttpResponse(httpStatus.value(),httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
	}
	
}
