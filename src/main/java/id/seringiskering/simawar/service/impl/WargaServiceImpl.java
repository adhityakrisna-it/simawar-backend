package id.seringiskering.simawar.service.impl;

import static id.seringiskering.simawar.constant.FileConstant.DIRECTORY_CREATED;
import static id.seringiskering.simawar.constant.FileConstant.DOT;
import static id.seringiskering.simawar.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static id.seringiskering.simawar.constant.FileConstant.JPG_EXTENSION;
import static id.seringiskering.simawar.constant.FileConstant.WARGA_FOLDER;
import static id.seringiskering.simawar.constant.FileConstant.FORWARD_SLASH;
import static id.seringiskering.simawar.constant.FileConstant.FAMILY_MEMBER_PROFILE_PATH;
import static id.seringiskering.simawar.constant.FileConstant.FAMILY_MEMBER_KTP_PATH;
import static id.seringiskering.simawar.constant.FileConstant.FAMILY_MEMBER_KK_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import id.seringiskering.simawar.entity.FamilyMember;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.exception.domain.InvalidDataException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.repository.FamilyMemberRepository;
import id.seringiskering.simawar.request.warga.SaveWargaRequest;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;
import id.seringiskering.simawar.service.WargaService;

@Service
public class WargaServiceImpl implements WargaService {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FamilyMemberRepository familyMemberRepository;

	@Override
	public List<ListWargaResponse> findFamilyMember(String username) {
		// TODO Auto-generated method stub
		
		List<FamilyMember> familyMember = familyMemberRepository.findAll();
		
		if (familyMember.size()>0) {
			List<ListWargaResponse> listWarga = new ArrayList<ListWargaResponse>();
			for (FamilyMember member: familyMember) {
				ListWargaResponse item = new ListWargaResponse();
				BeanUtils.copyProperties(member, item);
				listWarga.add(item);
			}
			return listWarga;
		}
		
		return null;
	}

	@Override
	public WargaResponse findFamilyMemberById(String username, Long id) {
		// TODO Auto-generated method stub
		Optional<FamilyMember> member = familyMemberRepository.findById(id);
		
		if (member.isPresent()) {
			FamilyMember warga = member.get();
			WargaResponse response = new WargaResponse();
			BeanUtils.copyProperties(warga, response);
			
			return response;
		}
		
		return null;
	}

	@Override
	public void saveDataWarga(
			   String mode, 
			   String username, 
			   SaveWargaRequest request, 
			   MultipartFile fotoWarga,
			   MultipartFile fotoKtp,
			   MultipartFile fotoKK) 
					   throws InvalidDataException, IOException, NotAnImageFileException {
		
		// TODO Auto-generated method stub
		
		validateDataWarga(request.getNoKtp());
		
		Optional<FamilyMember> warga;
		FamilyMember saveWarga = null;
		if (mode.equals("Add")) {
			saveWarga = new FamilyMember();
		}
		if (mode.equals("Edit")) {
			warga = familyMemberRepository.findById(request.getId());
			saveWarga = warga.get();
		}
		
		saveWarga.setName(request.getName());
		saveWarga.setPhoneNumber1(request.getPhoneNumber1());
		saveWarga.setPhoneNumber2(request.getPhoneNumber2());
		saveWarga.setPhoneNumber3(request.getPhoneNumber3());
		saveWarga.setAddressAsId(request.getAddressAsId());
		saveWarga.setReligion(request.getReligion());
		saveWarga.setKependudukanStatus(request.getKependudukanStatus());
		saveWarga.setDateAdd(request.getBirthDate());
		saveWarga.setWork(request.getWork());
		saveWarga.setFamilyStatus(request.getFamilyStatus());
		saveWarga.setSex(request.getSex());
		saveWarga.setNote(request.getNote());
		saveWarga.setBloodType(request.getBloodType());
		saveWarga.setLastEducation(request.getLastEducation());
		saveWarga.setNoKk(request.getNoKk());
		saveWarga.setNoKtp(request.getNoKtp());
		saveWarga.setBpjsNo(request.getBpjsNo());
		saveWarga.setKisNo(request.getKisNo());		
		
		if (fotoWarga != null) {
			saveImage(request.getNoKtp(), fotoWarga , "fotoProfile");
			saveWarga.setProfileUrl(FAMILY_MEMBER_PROFILE_PATH + request.getNoKtp()); 
		}
		if (fotoKtp != null) {
			saveImage(request.getNoKtp(), fotoKtp, "fotoKtp");
			saveWarga.setProfileUrl(FAMILY_MEMBER_KTP_PATH + request.getNoKtp());
		}
		if (fotoKK != null ) {
			saveImage(request.getNoKtp(), fotoKK, "fotoKK");
			saveWarga.setProfileUrl(FAMILY_MEMBER_KK_PATH + request.getNoKtp());
		}
		
		familyMemberRepository.save(saveWarga);
		
	}
	
	private void validateDataWarga(String dataKTP) throws InvalidDataException {
		// TODO Auto-generated method stub
		if (dataKTP.trim().equals(""))
		{
			throw new InvalidDataException("Data KTP tidak tidak boleh kosong");
		}
	}

	private void saveImage(String sIdWarga, MultipartFile image, String jenisFile) 
													throws IOException, NotAnImageFileException {
		
		// TODO Auto-generated method stub
		if (image != null) {

			if (!Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE)
					.contains(image.getContentType())) {
				throw new NotAnImageFileException(image.getOriginalFilename() + " is not an image file");
			}

			Path userFolder = Paths.get(WARGA_FOLDER + sIdWarga).toAbsolutePath().normalize();
			if (!Files.exists(userFolder)) {
				Files.createDirectories(userFolder);
				LOGGER.info(DIRECTORY_CREATED + userFolder);
			}
			Files.deleteIfExists(Paths.get(userFolder + sIdWarga + FORWARD_SLASH + jenisFile + DOT + JPG_EXTENSION));
			try {
				Files.copy(image.getInputStream(), userFolder.resolve(jenisFile + DOT + JPG_EXTENSION),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM);
		}
	}	
	

}
