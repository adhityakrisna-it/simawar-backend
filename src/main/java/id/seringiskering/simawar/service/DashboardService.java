package id.seringiskering.simawar.service;

import java.util.List;

import id.seringiskering.simawar.response.dashboard.CardResponse;
import id.seringiskering.simawar.response.dashboard.InfoIuranResponse;
import id.seringiskering.simawar.response.warga.InfoKeluargaResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;

public interface DashboardService {
	CardResponse findCardResponse(String username);
	InfoIuranResponse findRekapIuranResponse(String username);
	List<ListKeluargaResponse> findAllKeluarga(String username);
	InfoKeluargaResponse findFamilyById(String username, Long id);
}
