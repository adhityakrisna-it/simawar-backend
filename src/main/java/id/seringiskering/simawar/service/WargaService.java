package id.seringiskering.simawar.service;

import java.util.List;

import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;

public interface WargaService {
	List<ListWargaResponse> findFamilyMember(String username);
	WargaResponse findFamilyMemberById(String username, Long id);
}
