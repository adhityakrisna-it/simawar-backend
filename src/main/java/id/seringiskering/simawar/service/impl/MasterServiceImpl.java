package id.seringiskering.simawar.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.seringiskering.simawar.entity.MasterBlok;
import id.seringiskering.simawar.entity.MasterCluster;
import id.seringiskering.simawar.entity.MasterRole;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.repository.MasterBlokRepository;
import id.seringiskering.simawar.repository.MasterClusterRepository;
import id.seringiskering.simawar.repository.MasterRoleRepository;
import id.seringiskering.simawar.repository.UserRepository;
import id.seringiskering.simawar.response.master.MasterBlokResponse;
import id.seringiskering.simawar.service.MasterService;

@Service
public class MasterServiceImpl implements MasterService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	private MasterClusterRepository masterClusterRepository;
	private MasterBlokRepository masterBlokRepository;
	private MasterRoleRepository masterRoleRepository;
	private UserRepository userRepository;

	@Autowired
	public MasterServiceImpl(MasterClusterRepository masterClusterRepository, MasterBlokRepository masterBlokRepository,
			MasterRoleRepository masterRoleRepository, UserRepository userRepository) {
		super();
		this.masterClusterRepository = masterClusterRepository;
		this.masterBlokRepository = masterBlokRepository;
		this.masterRoleRepository = masterRoleRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<MasterCluster> findAllMasterCluster() {
		// TODO Auto-generated method stub
		return masterClusterRepository.findAll();
	}

	@Override
	public List<MasterBlokResponse> findMasterBlokByClusterId(String clusterId) throws DataNotFoundException {
		// TODO Auto-generated method stub
		List<MasterBlokResponse> blokResponse = getMasterBloksReponseFromMasterBlok(
				masterBlokRepository.findMasterBlokByClusterIdList(clusterId));
		return blokResponse;
	}

	private List<MasterBlokResponse> getMasterBloksReponseFromMasterBlok(List<MasterBlok> masterBlokList)
			throws DataNotFoundException {
		// TODO Auto-generated method stub
		if (null == masterBlokList || masterBlokList.size() == 0) {
			throw new DataNotFoundException("Master Blok tidak Ditemukan");
		}
		List<MasterBlokResponse> masterBlokResponse = new ArrayList<MasterBlokResponse>();
		for (MasterBlok blok : masterBlokList) {
			masterBlokResponse.add(
					new MasterBlokResponse(blok.getId().getBlokId(), blok.getBlokName(), blok.getId().getClusterId()));
		}
		return masterBlokResponse;
	}

	@Override
	public List<MasterRole> findAllMasterRole() {
		// TODO Auto-generated method stub
		return masterRoleRepository.findAll();
	}

	@Override
	public List<MasterRole> findAllMasterRoleByUsername(String username) {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByUsername(username);
		String role = user.getRole();
		if (role.equals("ROLE_SUPER_ADMIN")) {
			return masterRoleRepository.findAll();
		} else if (role.equals("ROLE_PENGURUS_RW")) {
			List<String> notIn = new ArrayList<String>();
			notIn.add("ROLE_SUPER_ADMIN");
			notIn.add("ROLE_ADMIN");
			return masterRoleRepository.findByRoleIdNotIn(notIn);
		} else if (role.equals("ROLE_PENGURUS_RT")) {
			List<String> notIn = new ArrayList<String>();
			notIn.add("ROLE_SUPER_ADMIN");
			notIn.add("ROLE_ADMIN");
			notIn.add("ROLE_PENGURUS_RW");
			return masterRoleRepository.findByRoleIdNotIn(notIn);
		}

		return null;
	}

}
