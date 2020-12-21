package id.seringiskering.simawar.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.seringiskering.simawar.entity.MasterBlok;
import id.seringiskering.simawar.entity.MasterCluster;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.repository.MasterBlokRepository;
import id.seringiskering.simawar.repository.MasterClusterRepository;
import id.seringiskering.simawar.response.master.MasterBlokResponse;
import id.seringiskering.simawar.service.MasterService;

@Service
public class MasterServiceImpl implements MasterService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private MasterClusterRepository	masterClusterRepository;
	private MasterBlokRepository masterBlokRepository;

	@Autowired
	public MasterServiceImpl(MasterClusterRepository masterClusterRepository, MasterBlokRepository masterBlokRepository) {
		super();
		this.masterClusterRepository = masterClusterRepository;
		this.masterBlokRepository = masterBlokRepository;
	}

	@Override
	public List<MasterCluster> findAllMasterCluster() {
		// TODO Auto-generated method stub
		return masterClusterRepository.findAll();
	}

	@Override
	public List<MasterBlokResponse> findMasterBlokByClusterId(String clusterId) throws DataNotFoundException {
		// TODO Auto-generated method stub
		List<MasterBlokResponse> blokResponse = getMasterBloksReponseFromMasterBlok(masterBlokRepository.findMasterBlokByClusterIdList(clusterId)); 
		return blokResponse;
	}

	private List<MasterBlokResponse> getMasterBloksReponseFromMasterBlok(List<MasterBlok> masterBlokList) throws DataNotFoundException {
		// TODO Auto-generated method stub
		if (null == masterBlokList || masterBlokList.size() == 0 ) {
			throw new DataNotFoundException("Master Blok tidak Ditemukan");
		}
		List<MasterBlokResponse> masterBlokResponse = new ArrayList<MasterBlokResponse>();
		for (MasterBlok blok: masterBlokList ) {
			masterBlokResponse.add(new MasterBlokResponse(
														blok.getId().getBlokId(), 
														blok.getBlokName(), 
														blok.getId().getClusterId()));
		}
		return masterBlokResponse;
	}
	
}
