package id.seringiskering.simawar.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import id.seringiskering.simawar.exception.domain.InvalidDataException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.request.warga.SaveWargaRequest;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;

public interface WargaService {
	List<ListWargaResponse> findFamilyMember(String username);
	WargaResponse findFamilyMemberById(String username, Long id);
	void saveDataWarga(String mode, 
					   String username, 
					   SaveWargaRequest request, 
					   MultipartFile fotoWarga,
					   MultipartFile fotoKtp,
					   MultipartFile fotoKK) throws InvalidDataException, IOException, NotAnImageFileException;
}
