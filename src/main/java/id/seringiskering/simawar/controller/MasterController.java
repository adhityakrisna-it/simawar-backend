package id.seringiskering.simawar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.seringiskering.simawar.entity.MasterCluster;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.response.master.MasterBlokResponse;
import id.seringiskering.simawar.service.MasterService;

@RestController
@RequestMapping(path = { "/master" })
public class MasterController {
	
	private MasterService masterService;

	@Autowired
	public MasterController(MasterService masterService) {
		this.masterService = masterService;
	}
	
	@GetMapping("/findAllMasterCluster")
	public ResponseEntity<List<MasterCluster>> findAllMasterCluster() {
		List<MasterCluster> masterCluster = masterService.findAllMasterCluster();
		return new ResponseEntity<> (masterCluster, HttpStatus.OK);
	}
	
	@GetMapping("/findMasterBlokByClusterId/{clusterId}")
	public ResponseEntity<List<MasterBlokResponse>> findMasterBlokByClusterId(@PathVariable("clusterId") String clusterId) throws DataNotFoundException {
		List<MasterBlokResponse> masterBlok = masterService.findMasterBlokByClusterId(clusterId);
		return new ResponseEntity<> (masterBlok, HttpStatus.OK);
	}
	
	

}
