package id.seringiskering.simawar.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.seringiskering.simawar.entity.Persil;
import id.seringiskering.simawar.repository.PersilRepository;
import id.seringiskering.simawar.service.PersilService;

@Service
public class PersilServiceImpl implements PersilService {
	
	@Autowired
	PersilRepository persilRepository;

	@Override
	public List<Persil> findPersilByUsername(String username) {
		// TODO Auto-generated method stub
		//return persilRepository.findAll();
		return persilRepository.findAllByOrderByBlokIdAscBlokNumberAsc();
	}

}
