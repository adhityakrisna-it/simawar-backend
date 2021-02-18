package id.seringiskering.simawar.service.impl;

import static id.seringiskering.simawar.constant.FileConstant.DIRECTORY_CREATED;
import static id.seringiskering.simawar.constant.FileConstant.DOT;
import static id.seringiskering.simawar.constant.FileConstant.FAMILY_MEMBER_KK_PATH;
import static id.seringiskering.simawar.constant.FileConstant.FAMILY_MEMBER_KTP_PATH;
import static id.seringiskering.simawar.constant.FileConstant.FAMILY_MEMBER_PROFILE_PATH;
import static id.seringiskering.simawar.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static id.seringiskering.simawar.constant.FileConstant.FORWARD_SLASH;
import static id.seringiskering.simawar.constant.FileConstant.JPG_EXTENSION;
import static id.seringiskering.simawar.constant.FileConstant.WARGA_FOLDER;
import static id.seringiskering.simawar.constant.FileConstant.KELUARGA_FOLDER;
import static id.seringiskering.simawar.constant.FileConstant.MEMBER_PROFILE_PATH;
import static id.seringiskering.simawar.constant.FileConstant.MEMBER_KK_PATH;
import static id.seringiskering.simawar.constant.FileConstant.MEMBER_HOME_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import id.seringiskering.simawar.entity.Family;
import id.seringiskering.simawar.entity.FamilyDeleted;
import id.seringiskering.simawar.entity.FamilyDeletedPK;
import id.seringiskering.simawar.entity.FamilyMember;
import id.seringiskering.simawar.entity.FamilyMemberDeleted;
import id.seringiskering.simawar.entity.FamilyMemberDeletedPK;
import id.seringiskering.simawar.entity.Persil;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.exception.domain.InvalidDataException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.repository.FamilyDeletedRepository;
import id.seringiskering.simawar.repository.FamilyMemberDeletedRepository;
import id.seringiskering.simawar.repository.FamilyMemberRepository;
import id.seringiskering.simawar.repository.FamilyRepository;
import id.seringiskering.simawar.repository.PersilRepository;
import id.seringiskering.simawar.repository.UserRepository;
import id.seringiskering.simawar.request.warga.FilterWargaRequest;
import id.seringiskering.simawar.request.warga.SaveKeluargaRequest;
import id.seringiskering.simawar.request.warga.SaveWargaRequest;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.response.warga.ListWargaResponse;
import id.seringiskering.simawar.response.warga.WargaResponse;
import id.seringiskering.simawar.service.WargaService;

@Service
public class WargaServiceImpl implements WargaService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private FamilyMemberRepository familyMemberRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FamilyRepository familyRepository;

	@Autowired
	private PersilRepository persilRepository;
	
	@Autowired
	private FamilyMemberDeletedRepository familyMemberDeletedRepository;
	
	@Autowired
	private FamilyDeletedRepository familyDeletedRepository;

	@Override
	public List<ListWargaResponse> findFamilyMember(String username) {
		// TODO Auto-generated method stub

		List<FamilyMember> familyMember = familyMemberRepository.findAll();

		if (familyMember.size() > 0) {
			List<ListWargaResponse> listWarga = new ArrayList<ListWargaResponse>();
			for (FamilyMember member : familyMember) {
				ListWargaResponse item = new ListWargaResponse();
				BeanUtils.copyProperties(member, item);

				if (member.getFamily() != null) {
					item.setFamilyName(member.getFamily().getFamilyName());
				}

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
	@Transactional
	public void saveDataWarga(String mode, String username, SaveWargaRequest request, MultipartFile fotoWarga,
			MultipartFile fotoKtp, MultipartFile fotoKK)
			throws InvalidDataException, IOException, NotAnImageFileException {

		// TODO Auto-generated method stub

		validateDataWarga(request.getNoKtp());

		User user = userRepository.findUserByUsername(username);

		Optional<FamilyMember> warga;
		FamilyMember saveWarga = null;
		if (mode.equals("Add")) {
			saveWarga = new FamilyMember();
			saveWarga.setUser2(user);
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
		saveWarga.setBirthDate(request.getBirthDate());
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
		saveWarga.setAddress(request.getAddress());
		saveWarga.setEmail(request.getEmail());

		if (fotoWarga != null) {
			saveImage(request.getNoKtp(), fotoWarga, "fotoProfile");
			LOGGER.info("KTP PATH : " + FAMILY_MEMBER_PROFILE_PATH + request.getNoKtp());
			saveWarga.setProfileUrl(FAMILY_MEMBER_PROFILE_PATH + request.getNoKtp());
		}
		if (fotoKtp != null) {
			saveImage(request.getNoKtp(), fotoKtp, "fotoKtp");
			saveWarga.setKtpUrl(FAMILY_MEMBER_KTP_PATH + request.getNoKtp());
		}
		if (fotoKK != null) {
			saveImage(request.getNoKtp(), fotoKK, "fotoKK");
			saveWarga.setKkUrl(FAMILY_MEMBER_KK_PATH + request.getNoKtp());
		}

		familyMemberRepository.save(saveWarga);

	}	

	@Override
	@Transactional
	public ListWargaResponse saveWarga(String mode, String username, SaveWargaRequest request, MultipartFile fotoWarga,
			MultipartFile fotoKtp, MultipartFile fotoKK)
			throws InvalidDataException, IOException, NotAnImageFileException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		validateDataWarga(request.getNoKtp());

		User user = userRepository.findUserByUsername(username);

		Optional<FamilyMember> warga;
		FamilyMember saveWarga = null;
		if (mode.equals("Add")) {
			saveWarga = new FamilyMember();
			saveWarga.setUser2(user);
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
		saveWarga.setBirthDate(request.getBirthDate());
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
		saveWarga.setAddress(request.getAddress());
		saveWarga.setEmail(request.getEmail());

		if (fotoWarga != null) {
			saveImage(request.getNoKtp(), fotoWarga, "fotoProfile");
			LOGGER.info("KTP PATH : " + FAMILY_MEMBER_PROFILE_PATH + request.getNoKtp());
			saveWarga.setProfileUrl(FAMILY_MEMBER_PROFILE_PATH + request.getNoKtp());
		}
		if (fotoKtp != null) {
			saveImage(request.getNoKtp(), fotoKtp, "fotoKtp");
			saveWarga.setKtpUrl(FAMILY_MEMBER_KTP_PATH + request.getNoKtp());
		}
		if (fotoKK != null) {
			saveImage(request.getNoKtp(), fotoKK, "fotoKK");
			saveWarga.setKkUrl(FAMILY_MEMBER_KK_PATH + request.getNoKtp());
		}

		FamilyMember result = familyMemberRepository.save(saveWarga);
		
		ListWargaResponse response = new ListWargaResponse();
		BeanUtils.copyProperties(result, response);
		
		return response;
	}

	@Override
	public List<ListWargaResponse> findFamilyMemberByFilter(FilterWargaRequest filter) {
		// TODO Auto-generated method stub

		LOGGER.info("Filter with data : {} ", filter.toString());

		List<FamilyMember> warga = familyMemberRepository.findAll(new Specification<FamilyMember>() {

			@Override
			public Predicate toPredicate(Root<FamilyMember> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				// TODO Auto-generated method stub
				List<Predicate> predicates = new ArrayList<>();

				if (filter.getIsUmur().equals("true")) {
					Calendar cal = Calendar.getInstance();
					Date today = cal.getTime();
					cal.add(Calendar.YEAR, -Integer.parseInt(filter.getUmurAwal())); // to get previous year add -1
					Date awalUmur = cal.getTime();
					cal.add(Calendar.YEAR, -Integer.parseInt(filter.getUmurAkhir())); // to get previous year add -1
					Date akhirUmur = cal.getTime();

					LOGGER.info("Filter Awal : {} ", awalUmur);
					LOGGER.info("Filter Akhir : {} ", akhirUmur);

					predicates.add(criteriaBuilder.between(root.get("birthDate"), akhirUmur, awalUmur));

				}

				if (filter.getIsJenisKelamin().equals("true")) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("sex"), filter.getSex())));
				}

				if (filter.getIsReligion().equals("true")) {
					predicates.add(
							criteriaBuilder.and(criteriaBuilder.equal(root.get("religion"), filter.getReligion())));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});

		List<ListWargaResponse> listWarga = new ArrayList<ListWargaResponse>();
		for (FamilyMember member : warga) {
			ListWargaResponse item = new ListWargaResponse();
			BeanUtils.copyProperties(member, item);
			listWarga.add(item);
		}

		return listWarga;

	}

	@Override
	public List<ListKeluargaResponse> findFamily(String username) {
		// TODO Auto-generated method stub

		List<Family> listFamily = familyRepository.findAll();
		if (listFamily.size() > 0) {

			List<ListKeluargaResponse> listKeluarga = new ArrayList<ListKeluargaResponse>();
			for (Family family : listFamily) {
				ListKeluargaResponse item = new ListKeluargaResponse();
				BeanUtils.copyProperties(family, item);
				
				if (family.getFamilyMembers().size()>0) {
					List<ListWargaResponse> members = new ArrayList<ListWargaResponse>();
					for (FamilyMember member: family.getFamilyMembers()) {
						ListWargaResponse listwarga = new ListWargaResponse();
						BeanUtils.copyProperties(member, listwarga);
						
						members.add(listwarga);
					}
					item.setFamilyMember(members);
				}
				
				item.setAddress(family.getCluster().toUpperCase() + " " + family.getBlok() + " " + family.getNomor());
				
				listKeluarga.add(item);
			}
			return listKeluarga;

		}

		return null;
	}

	@Override
	public ListKeluargaResponse findFamilyById(Long id) {
		// TODO Auto-generated method stub

		Optional<Family> cekfamily = familyRepository.findById(id);
		Family family = cekfamily.get();

		ListKeluargaResponse item = new ListKeluargaResponse();
		BeanUtils.copyProperties(family, item);
		
		if (family.getFamilyMembers().size()>0) {
			List<ListWargaResponse> members = new ArrayList<ListWargaResponse>();
			for (FamilyMember member: family.getFamilyMembers()) {
				ListWargaResponse listwarga = new ListWargaResponse();
				BeanUtils.copyProperties(member, listwarga);
				
				members.add(listwarga);
			}
			item.setFamilyMember(members);
		}
		
		item.setAddress(family.getCluster().toUpperCase() + " " + family.getBlok() + " " + family.getNomor());		

		return item;
	}

	@Override
	@Transactional
	public void saveKeluarga(String mode, String username, SaveKeluargaRequest request) throws InvalidDataException {
		// TODO Auto-generated method stub
		validateDataKeluarga(request.getFamilyName());
		User user = userRepository.findUserByUsername(username);
		Family save = null;
		if (mode.equals("Add")) {
			save = new Family();
			save.setUser1(user);
			save.setUser2(user);
		} else if (mode.equals("Edit")) {
			Optional<Family> cek = familyRepository.findById(request.getId());
			save = cek.get();
			save.setUser1(user);
			
			Optional<List<FamilyMember>> resetMember = familyMemberRepository.findByFamilyId(request.getId());
			if (resetMember.isPresent()) {
				List<FamilyMember> familymembers = resetMember.get();
				for (FamilyMember familymember: familymembers) {
					familymember.setFamily(null);
				}
			}
			
		}

		save.setFamilyName(request.getFamilyName());
		save.setKepemilikanStatus(request.getKepemilikanStatus());
		save.setNote(request.getNote());
		save.setGreeting(request.getGreeting());

		Optional<Persil> persilcek = persilRepository.findById(request.getPersilId());
		if (persilcek.isPresent()) {
			save.setPersil(persilcek.get());
			save.setRt(persilcek.get().getRtId().toString());
			save.setRw(persilcek.get().getRwId().toString());
			save.setCluster(persilcek.get().getClusterId());
			save.setBlok(persilcek.get().getBlokId());
			save.setNomor(String.valueOf(persilcek.get().getBlokNumber()));
		}

		if (request.getFamilyMemberId().size() > 0) {
			for (String id : request.getFamilyMemberId()) {
				Optional<FamilyMember> cekFamily = familyMemberRepository.findById(Long.valueOf(id));
				FamilyMember family = cekFamily.get();
				family.setFamily(save);
			}
		}

		familyRepository.save(save);

	}

	@Override
	@Transactional
	public ListKeluargaResponse saveProfileKeluarga(String username, Long id, MultipartFile fileFoto) throws IOException, NotAnImageFileException {
		// TODO Auto-generated method stub

		Optional<Family> familyCek = familyRepository.findById(id);
		if (familyCek.isPresent()) {
			saveImageKeluarga(id.toString(), fileFoto, "profilekeluarga");
			Family family = familyCek.get();
			family.setProfileUrl(MEMBER_PROFILE_PATH + id.toString());
			familyRepository.save(family);
			
			ListKeluargaResponse item = new ListKeluargaResponse();
			BeanUtils.copyProperties(family, item);
			
			return item;
			
		}

		return null;
	}
	
	@Override
	@Transactional
	public ListKeluargaResponse saveKKKeluarga(String username, Long id, MultipartFile fileFoto) throws IOException, NotAnImageFileException {
		// TODO Auto-generated method stub

		Optional<Family> familyCek = familyRepository.findById(id);
		if (familyCek.isPresent()) {
			saveImageKeluarga(id.toString(), fileFoto, "kkkeluarga");
			Family family = familyCek.get();
			family.setKkUrl(MEMBER_KK_PATH + id.toString());
			familyRepository.save(family);
			
			ListKeluargaResponse item = new ListKeluargaResponse();
			BeanUtils.copyProperties(family, item);
			
			return item;
			
		}

		return null;
	}
	

	@Override
	@Transactional
	public ListKeluargaResponse saveRumahKeluarga(String username, Long id, MultipartFile fileFoto)
			throws IOException, NotAnImageFileException {
		Optional<Family> familyCek = familyRepository.findById(id);
		if (familyCek.isPresent()) {
			saveImageKeluarga(id.toString(), fileFoto, "rumahkeluarga");
			Family family = familyCek.get();
			family.setFamilyProfileUrl(MEMBER_HOME_PATH + id.toString());
			familyRepository.save(family);
			
			ListKeluargaResponse item = new ListKeluargaResponse();
			BeanUtils.copyProperties(family, item);
			
			return item;
			
		}

		return null;
	}

	@Override
	public ListWargaResponse findListFamilyMemberById(String username, Long id) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Optional<FamilyMember> member = familyMemberRepository.findById(id);

		if (member.isPresent()) {
			FamilyMember warga = member.get();
			ListWargaResponse response = new ListWargaResponse();
			BeanUtils.copyProperties(warga, response);

			return response;
		}

		return null;		
	}
	

	@Override
	@Transactional
	public void deleteDataWarga(String username, Long id) {
		// TODO Auto-generated method stub
		Optional<FamilyMember> cekmember = familyMemberRepository.findById(id);
		
		if (cekmember.isPresent()) {
			FamilyMember familymember = cekmember.get();
			FamilyMemberDeleted deleted = new FamilyMemberDeleted();
			FamilyMemberDeletedPK pkdeleted = new FamilyMemberDeletedPK();
			pkdeleted.setId(id);
			pkdeleted.setDateLog(new Date());
			deleted.setId(pkdeleted);
			
			BeanUtils.copyProperties(familymember, deleted);
			deleted.setUserIdAdd(familymember.getUser1() == null ? null : familymember.getUser1().getUserId());
			deleted.setUserIdEdit(familymember.getUser2() == null ? null : familymember.getUser2().getUserId());
			
			familyMemberDeletedRepository.save(deleted);
			
			familyMemberRepository.delete(familymember);			
			
		}
	}	

	@Override
	@Transactional
	public void deleteDataKeluarga(String username, Long id) {
		// TODO Auto-generated method stub
		Optional<Family> cekfamily = familyRepository.findById(id);
		User cekuser = userRepository.findUserByUsername(username);
		
		if (cekfamily.isPresent()) {
			Family family = cekfamily.get();
			Optional<List<FamilyMember>> cekfamilymember = familyMemberRepository.findByFamilyId(id);
			if (cekfamilymember.isPresent()) {
				for (FamilyMember familymember: cekfamilymember.get()) {
					familymember.setFamily(null);
				}
			}
			
			FamilyDeleted deleted = new FamilyDeleted();
			FamilyDeletedPK pkdeleted = new FamilyDeletedPK();
			pkdeleted.setId(id);
			pkdeleted.setDateLog(new Date());
			
			BeanUtils.copyProperties(family, deleted);
			
			deleted.setUser(cekuser);
			deleted.setId(pkdeleted);
			familyDeletedRepository.save(deleted);
			
			familyRepository.delete(family);
			
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

	private void saveImageKeluarga(String sId, MultipartFile image, String filename)
			throws IOException, NotAnImageFileException {

		// TODO Auto-generated method stub
		if (image != null) {

			if (!Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE)
					.contains(image.getContentType())) {
				throw new NotAnImageFileException(image.getOriginalFilename() + " is not an image file");
			}

			Path userFolder = Paths.get(KELUARGA_FOLDER + sId).toAbsolutePath().normalize();
			if (!Files.exists(userFolder)) {
				Files.createDirectories(userFolder);
				LOGGER.info(DIRECTORY_CREATED + userFolder);
			}
			Files.deleteIfExists(Paths.get(userFolder + sId + FORWARD_SLASH + filename + DOT + JPG_EXTENSION));
			try {
				Files.copy(image.getInputStream(), userFolder.resolve(filename + DOT + JPG_EXTENSION),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM);
		}
	}
	
	private void validateDataWarga(String dataKTP) throws InvalidDataException {
		// TODO Auto-generated method stub
		if (dataKTP == null || dataKTP.trim().equals("")) {
			throw new InvalidDataException("Data KTP tidak tidak boleh kosong");
		}
	}

	private void validateDataKeluarga(String familyName) throws InvalidDataException {
		// TODO Auto-generated method stub
		if (familyName == null || familyName.trim().equals("")) {
			throw new InvalidDataException("Nama keluarga tidak tidak boleh kosong");
		}
	}
	
}
