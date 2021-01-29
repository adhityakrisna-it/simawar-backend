package id.seringiskering.simawar.service;

import java.util.List;

import id.seringiskering.simawar.entity.MasterBloodType;
import id.seringiskering.simawar.entity.MasterCluster;
import id.seringiskering.simawar.entity.MasterFamilyStatus;
import id.seringiskering.simawar.entity.MasterLastEducation;
import id.seringiskering.simawar.entity.MasterPekerjaan;
import id.seringiskering.simawar.entity.MasterRole;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.response.master.MasterBlokResponse;

public interface MasterService {

	List<MasterCluster> findAllMasterCluster(); 
	List<MasterBlokResponse> findMasterBlokByClusterId(String clusterId) throws DataNotFoundException;
	List<MasterRole> findAllMasterRole();
	List<MasterRole> findAllMasterRoleByUsername(String username);
	List<MasterBloodType> findAllMasterBloodType();
	List<MasterFamilyStatus> findAllMasterFamilyStatus();
	List<MasterLastEducation> findAllMasterLastEducation();
	List<MasterPekerjaan> findAllMasterPekerjaan(); 
	
}
