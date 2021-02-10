package id.seringiskering.simawar.service;

import java.util.List;

import id.seringiskering.simawar.entity.Persil;

public interface PersilService {
	
	List<Persil> findPersilByUsername(String username);
	
}
