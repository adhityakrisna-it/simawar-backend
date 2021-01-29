package id.seringiskering.simawar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.seringiskering.simawar.entity.MasterBloodType;
import id.seringiskering.simawar.entity.MasterCluster;
import id.seringiskering.simawar.entity.MasterFamilyStatus;
import id.seringiskering.simawar.entity.MasterLastEducation;
import id.seringiskering.simawar.entity.MasterPekerjaan;
import id.seringiskering.simawar.entity.MasterRole;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.response.master.MasterBlokResponse;
import id.seringiskering.simawar.service.MasterService;

@RestController
@RequestMapping(path = { "/master" })
public class MasterController {
	
	private MasterService masterService;
	private JwtAuthorizationFilter jwtAuthorizationFilter;

	@Autowired
	public MasterController(MasterService masterService,
			JwtAuthorizationFilter jwtAuthorizationFilter) {
		this.masterService = masterService;
		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
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
	
	@GetMapping("/findMasterRole")
	public ResponseEntity<List<MasterRole>> findMasterRole() {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<MasterRole> masterRole = masterService.findAllMasterRoleByUsername(username);
		return new ResponseEntity<List<MasterRole>>(masterRole, HttpStatus.OK);
	}
	
	@GetMapping("/findMasterBloodType")
	public ResponseEntity<List<MasterBloodType>> findMasterBloodType()
	{
		List<MasterBloodType> masterBloodType = masterService.findAllMasterBloodType();
		return new ResponseEntity<List<MasterBloodType>>(masterBloodType, HttpStatus.OK);
	}
	
	@GetMapping("/findMasterFamilyStatus")
	public ResponseEntity<List<MasterFamilyStatus>> findMasterFamilyStatus() {
		List<MasterFamilyStatus> ret = masterService.findAllMasterFamilyStatus();
		return new ResponseEntity<List<MasterFamilyStatus>>(ret, HttpStatus.OK);
	}
	
	@GetMapping("/findMasterPekerjaan") 
	public ResponseEntity<List<MasterPekerjaan>> findMasterPekerjaan()
	{
		List<MasterPekerjaan> ret = masterService.findAllMasterPekerjaan();
		return new ResponseEntity<List<MasterPekerjaan>>(ret, HttpStatus.OK);
	}
	
	@GetMapping("/findMasterLastEducation")
	public ResponseEntity<List<MasterLastEducation>> findMasterLastEducation() {
		List<MasterLastEducation> ret = masterService.findAllMasterLastEducation();
		return new ResponseEntity<List<MasterLastEducation>>(ret, HttpStatus.OK);
	}
		
}
