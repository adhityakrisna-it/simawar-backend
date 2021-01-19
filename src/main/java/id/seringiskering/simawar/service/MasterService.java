package id.seringiskering.simawar.service;

import java.util.List;

import id.seringiskering.simawar.entity.MasterCluster;
import id.seringiskering.simawar.entity.MasterRole;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.response.master.MasterBlokResponse;

public interface MasterService {

	List<MasterCluster> findAllMasterCluster(); 
	List<MasterBlokResponse> findMasterBlokByClusterId(String clusterId) throws DataNotFoundException;
	List<MasterRole> findAllMasterRole();
	List<MasterRole> findAllMasterRoleByUsername(String username);
	
}
