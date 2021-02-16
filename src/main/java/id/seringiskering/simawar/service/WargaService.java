package id.seringiskering.simawar.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import id.seringiskering.simawar.exception.domain.InvalidDataException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.request.warga.FilterWargaRequest;
import id.seringiskering.simawar.request.warga.SaveKeluargaRequest;
import id.seringiskering.simawar.request.warga.SaveWargaRequest;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;

public interface WargaService {
	List<ListWargaResponse> findFamilyMember(String username);
	WargaResponse findFamilyMemberById(String username, Long id);
	ListWargaResponse findListFamilyMemberById(String username, Long id);
	void saveDataWarga(String mode, 
					   String username, 
					   SaveWargaRequest request, 
					   MultipartFile fotoWarga,
					   MultipartFile fotoKtp,
					   MultipartFile fotoKK) throws InvalidDataException, IOException, NotAnImageFileException;
	ListWargaResponse saveWarga(String mode, 
			   String username, 
			   SaveWargaRequest request, 
			   MultipartFile fotoWarga,
			   MultipartFile fotoKtp,
			   MultipartFile fotoKK) throws InvalidDataException, IOException, NotAnImageFileException;
	
	List<ListWargaResponse> findFamilyMemberByFilter(FilterWargaRequest filter);
	List<ListKeluargaResponse> findFamily(String username);
	ListKeluargaResponse findFamilyById(Long id);
	void saveKeluarga(String mode, String username,
					  SaveKeluargaRequest request) throws InvalidDataException;	
	ListKeluargaResponse saveProfileKeluarga(String username, Long id, MultipartFile fileFoto) 
			throws IOException, NotAnImageFileException;
	ListKeluargaResponse saveKKKeluarga(String username, Long id, MultipartFile fileFoto) 
			throws IOException, NotAnImageFileException;
	
	void deleteDataWarga(String username, Long id);
	void deleteDataKeluarga(String username, Long id);
	
}
