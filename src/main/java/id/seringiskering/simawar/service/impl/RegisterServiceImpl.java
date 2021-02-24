package id.seringiskering.simawar.service.impl;

import static id.seringiskering.simawar.constant.UserImplConstant.EMAIL_ALREADY_EXIST;
import static id.seringiskering.simawar.constant.UserImplConstant.USERNAME_ALREADY_EXIST;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.seringiskering.simawar.constant.Authority;
import id.seringiskering.simawar.constant.FileConstant;
import id.seringiskering.simawar.entity.Family;
import id.seringiskering.simawar.entity.FamilyUserOwner;
import id.seringiskering.simawar.entity.FamilyUserOwnerPK;
import id.seringiskering.simawar.entity.Persil;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.entity.UserPersil;
import id.seringiskering.simawar.entity.UserRegister;
import id.seringiskering.simawar.enumeration.Role;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.UnauthorizedException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.function.StringManipulation;
import id.seringiskering.simawar.profile.UserProfile;
import id.seringiskering.simawar.repository.FamilyRepository;
import id.seringiskering.simawar.repository.FamilyUserOwnerRepository;
import id.seringiskering.simawar.repository.PersilRepository;
import id.seringiskering.simawar.repository.UserPersilRepository;
import id.seringiskering.simawar.repository.UserRegisterRepository;
import id.seringiskering.simawar.repository.UserRepository;
import id.seringiskering.simawar.request.registrasi.UserRegistrationRequest;
import id.seringiskering.simawar.response.register.UserRegisterResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	private static final String GAGAL_APPROVAL_DISAPPROVAL_USER = "Gagal approval/disapproval user";

	private static final String EMAIL_EXIST_REGISTER = "Email ini sedang dalam proses approval";

	private static final String USERNAME_EXIST_REGISTER = "Username ini sedang dalam proses approval";

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	private UserRegisterRepository userRegisterRepository;
	private UserRepository userRepository;
	private PersilRepository persilRepository;
	private FamilyUserOwnerRepository familyUserOwnerRepository;
	private FamilyRepository familyRepository;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public RegisterServiceImpl(UserRegisterRepository userRegisterRepository, 
							   BCryptPasswordEncoder passwordEncoder,
							   UserRepository userRepository, 
							   PersilRepository persilRepository,
							   FamilyUserOwnerRepository familyUserOwnerRepository,
							   FamilyRepository familyRepository) {
		this.userRegisterRepository = userRegisterRepository;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.persilRepository = persilRepository;
		this.familyUserOwnerRepository = familyUserOwnerRepository;
		this.familyRepository = familyRepository;
	}

	@Override
	@Transactional
	public void registerNewUser(UserRegistrationRequest request) throws UsernameExistException, EmailExistException {
		
		// TODO Auto-generated method stub		
		validateEntriNewUsernameAndEmail(request.getUsername(), request.getEmail());
		
		String[] homeNumber = parseHomeNumber(request.getHomeNumber());
		Integer blokNumber = Integer.parseInt(homeNumber[0]);
		String blokIdentity = homeNumber[1];
		
		UserRegister userRegister = new UserRegister();
		userRegister.setFirstName(request.getFirstName());
		userRegister.setLastName(request.getLastName());
		userRegister.setUsername(request.getUsername());
		userRegister.setEmail(request.getEmail());
		userRegister.setClusterId(request.getClusterId());
		userRegister.setBlokNumber(blokNumber);
		userRegister.setBlokIdentity(blokIdentity);
		userRegister.setPassword(encodePassword(request.getPassword()));
		userRegister.setBlokId(request.getBlokId());
		userRegister.setHomeNumber(request.getHomeNumber());
		
		List<Persil> persil = persilRepository.findPersilByClusterIdAndBlokIdAndBlokNumberAndBlokIdentity
											(request.getClusterId(), 
													request.getBlokId(), 
													blokNumber, 
													blokIdentity);

		userRegister.setRtId(Integer.parseInt(request.getRt()));
		userRegister.setRwId(Integer.parseInt(request.getRw()));

		if (persil.size() > 0) {
			Persil p = persil.get(0);
			userRegister.setPersilId(p.getPersilId());
			userRegister.setKelurahanId(p.getKelurahanId());
			userRegister.setBlokId(p.getBlokId());
		}
		
		userRegisterRepository.save(userRegister);

	}
	
	@Override
	public List<UserRegister> findUserRegisterForApprove(String username) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return getUserRegisterByUsername(username);
	}
	
	@Override
	@Transactional
	public void approveUserRegister(
				String username, 
				Long id, 
				String role, 
				String cluster, 
				String blok, 
				String nomor, 
				String nomorTambahan,
 				String dataRW,
 				String dataRT,
 				String rw,
 				String rt,
 				String familyId) 
				throws JsonProcessingException, UnauthorizedException, DataNotFoundException 
	{
		// TODO Auto-generated method stub
		validateUsernameAndId(username, id);
		
		Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
		
		if (!userRegister.isPresent())
		{
			throw new DataNotFoundException(GAGAL_APPROVAL_DISAPPROVAL_USER);
		}
		
		User user = userRepository.findUserByUsername(username);
		
		UserRegister saveUserRegister = userRegister.get();
		saveUserRegister.setRegisterStatus("setuju");
		saveUserRegister.setUserId(user.getUserId());
		userRegisterRepository.save(saveUserRegister);
		
		User userNew = new User();
		String userId = generateUserId(); 
		userNew.setUserId(userId);
		userNew.setFirstName(saveUserRegister.getFirstName());
		userNew.setLastName(saveUserRegister.getLastName());
		userNew.setUsername(saveUserRegister.getUsername());
		userNew.setEmail(saveUserRegister.getEmail());
		userNew.setPassword(saveUserRegister.getPassword());
		userNew.setActive(true);
		userNew.setNotLocked(true);
		userNew.setRole(getRoleEnumName(role).name());
		userNew.setAuthorities(getRoleEnumName(role).getAuthorities());        
		userNew.setProfileImageUrl(getTemporaryProfileImageUrl(saveUserRegister.getUsername()));
		
		Integer iRt;
		Integer iRw;
		
		if (dataRT == null || dataRT.equals("")) {
			iRt = null;
		} else {
			iRt = Integer.valueOf(dataRT);
		}
		
		if (dataRW == null || dataRW.equals("")) {
			iRw = null;
		} else {
			iRw = Integer.valueOf(dataRW);
		}
		
		
		userNew.setUserDataProfile(
				getUserProfile(
						role, 
						iRt, 
						iRw, 
						saveUserRegister.getKelurahanId(), 
						cluster,
						blok,
						nomor,
						nomorTambahan
						)
				);

		UserPersil userPersil = new UserPersil();
		userPersil.setUserId(userNew.getUserId());
		userPersil.setUser(userNew);
		userPersil.setBlok(blok);
		userPersil.setCluster(cluster);
		userPersil.setNomor(nomor);
		userPersil.setNomorTambahan(nomorTambahan);
		userPersil.setRt(rt);
		userPersil.setRw(rw);

		userNew.setUserPersil(userPersil);
        userRepository.save(userNew);
        
        if (familyId != null) {
        	FamilyUserOwnerPK idowner = new FamilyUserOwnerPK();
        	idowner.setId(Long.parseLong(familyId));
        	idowner.setUserId(userId);
        	FamilyUserOwner familyOwner = new FamilyUserOwner();
        	familyOwner.setId(idowner);
        	familyUserOwnerRepository.save(familyOwner);
        }        
        
	}
	

	@Override
	@Transactional
	public void disapproveUserRegister(String username, Long id)
			throws JsonProcessingException, UnauthorizedException, DataNotFoundException {
		// TODO Auto-generated method stub
		validateUsernameAndId(username, id);
		
		Optional<UserRegister> userRegister = userRegisterRepository.findById(id);
		
		if (!userRegister.isPresent())
		{
			throw new DataNotFoundException(GAGAL_APPROVAL_DISAPPROVAL_USER);
		}
		
		User user = userRepository.findUserByUsername(username);
		
		UserRegister saveUserRegister = userRegister.get();
		saveUserRegister.setRegisterStatus("tolak");
		saveUserRegister.setUserId(user.getUserId());
		userRegisterRepository.save(saveUserRegister);
		
	}	
	
	private String getUserProfile(String role, Integer rtId, Integer rwId, 
								  String kelurahanId, String clusterId, 
								  String blok, String nomor, String nomorTambahan) throws JsonProcessingException {
		
		UserProfile userProfile = new UserProfile();
		
		if (role.equals("ROLE_USER")) {
			userProfile.setCluster(clusterId);
			userProfile.setRt(String.valueOf(rtId));
			userProfile.setRw(String.valueOf(rtId));
			userProfile.setKelurahan(kelurahanId);
			userProfile.setBlok(blok);
			userProfile.setNomor(nomor);
		} else if (role.equals("ROLE_SUPER_ADMIN")) {
			userProfile.setCluster("ALL");
			userProfile.setRt("ALL");
			userProfile.setRw("ALL");
			userProfile.setKelurahan("ALL");
			userProfile.setBlok(blok);
			userProfile.setNomor(nomor);
		} else if (role.equals("ROLE_WARGA")) {
			userProfile.setCluster(clusterId);
			userProfile.setKelurahan("ALL");
			userProfile.setBlok(blok);
			userProfile.setNomor(nomor);
			userProfile.setNomorTambahan(nomorTambahan);
		} else if (role.equals("ROLE_PENGURUS_RT")) {
			userProfile.setCluster(clusterId);
			userProfile.setRt(rtId.toString());
		} else if (role.equals("ROLE_PENGURUS_RW")) {
			userProfile.setCluster(clusterId);
			userProfile.setRw(rwId.toString());			
		}
		else {
			userProfile.setCluster(clusterId);
			userProfile.setRt(String.valueOf(rtId));
			userProfile.setRw(String.valueOf(rwId));
			userProfile.setKelurahan(kelurahanId);
			userProfile.setBlok(blok);
			userProfile.setNomor(nomor);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonProfile = mapper.writeValueAsString(userProfile);
		
		return jsonProfile;
	}
	
	private String generateUserId() {
		// TODO Auto-generated method stub
		return RandomStringUtils.randomNumeric(10);
	}	
	
	private Role getRoleEnumName(String role) {
		// TODO Auto-generated method stub
		return Role.valueOf(role.toUpperCase());
	}
	
	private String getTemporaryProfileImageUrl(String username) {
		// TODO Auto-generated method stub
		//return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.DEFAULT_USER_IMAGE_PATH + username).toUriString();
		return FileConstant.DEFAULT_USER_IMAGE_PATH + username;
	}	
	
	private List<UserRegister> getUserRegisterByUsername(String username) throws JsonProcessingException {
		User user = userRepository.findUserByUsername(username);
		ObjectMapper mapper = new ObjectMapper();
		UserProfile userProfile = mapper.readValue(user.getUserDataProfile(), UserProfile.class);
		
		LOGGER.info("ROLE FOR USER : " + username + "=" + user.getRole());
		
		if (user.getRole().equals("ROLE_PENGURUS_RT")) {
			List<UserRegister> userRegister = userRegisterRepository.findUserRegisterByRegisterStatusAndRtId("entri", Integer.parseInt(userProfile.getRt()));
			return userRegister;
		} else if (user.getRole().equals("ROLE_PENGURUS_RW")) {
			List<UserRegister> userRegister = userRegisterRepository.findUserRegisterByRegisterStatusAndRwId("entri", Integer.parseInt(userProfile.getRw()));
			return userRegister;
		} else if (user.getRole().equals("ROLE_SUPER_ADMIN")) {
			List<UserRegister> userRegister = userRegisterRepository.findUserRegisterByRegisterStatus("entri");
			return userRegister;
		}else {
			List<UserRegister> userRegister = userRegisterRepository.findUserRegisterByRegisterStatusAndClusterId("entri", userProfile.getCluster());
			return userRegister;
		}
		
	}
	
	private void validateUsernameAndId(String username, Long id) throws JsonProcessingException, UnauthorizedException {
		// TODO Auto-generated method stub
		
		List<UserRegister> lstUserRegister = getUserRegisterByUsername(username);
		if (lstUserRegister.size()==0) {
			throw new UnauthorizedException(GAGAL_APPROVAL_DISAPPROVAL_USER);
		}
		
		isIdUserRegisterExist(lstUserRegister, id);
		
	}

	private void isIdUserRegisterExist(List<UserRegister> lstUserRegister, Long id) throws UnauthorizedException {
		// TODO Auto-generated method stub
		for (UserRegister userregister: lstUserRegister ) {
			if (userregister.getId().equals(id))
			{
				return;
			}
		}
		throw new UnauthorizedException(GAGAL_APPROVAL_DISAPPROVAL_USER);
	}

	private String encodePassword(String password) {
		// TODO Auto-generated method stub
		return passwordEncoder.encode(password);
	}

	private User validateEntriNewUsernameAndEmail(String newUsername, String newEmail)
			throws UsernameExistException, EmailExistException {
		// TODO Auto-generated method stub
		User userByNewUsername = findUserByUsername(newUsername);
		User userByNewEmail = findUserByEmail(newEmail);

		if (userByNewUsername != null) {
			throw new UsernameExistException(USERNAME_ALREADY_EXIST);
		}
		if (userByNewEmail != null) {
			throw new EmailExistException(EMAIL_ALREADY_EXIST);
		}
		
		UserRegister userRegisterUsername = findUserRegisterByUsername(newUsername);
		UserRegister userRegisterEmail = findUserRegisterByEmail(newEmail);
		
		if (userRegisterUsername != null) {
			throw new UsernameExistException(USERNAME_EXIST_REGISTER);
		}
		
		if (userRegisterEmail != null) {
			throw new EmailExistException(EMAIL_EXIST_REGISTER);
		}

		return null;
		
	}
	
	private User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findUserByUsername(username);
	}

	private User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findUserByEmail(email);
	}
	
	private UserRegister findUserRegisterByUsername(String username) {
		// TODO Auto-generated method stub
		return userRegisterRepository.findUserRegisterByUsernameAndRegisterStatus(username,"entri");
	}

	private UserRegister findUserRegisterByEmail(String email) {
		// TODO Auto-generated method stub
		return userRegisterRepository.findUserRegisterByEmailAndRegisterStatus(email, "entri");
	}

	@Override
	public List<UserRegisterResponse> findUserRegisterForApproval(String username) throws JsonProcessingException {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByUsername(username);
		ObjectMapper mapper = new ObjectMapper();
		UserProfile userProfile = mapper.readValue(user.getUserDataProfile(), UserProfile.class);
		
		LOGGER.info("ROLE FOR USER : " + username + "=" + user.getRole());
		
		List<UserRegister> userRegister;
		if (user.getRole().equals("ROLE_PENGURUS_RT")) {
			userRegister = userRegisterRepository.findUserRegisterByRegisterStatusAndRtId("entri", userProfile.getRt()==null ? null : Integer.parseInt(userProfile.getRt()));
		} else if (user.getRole().equals("ROLE_PENGURUS_RW")) {
			userRegister = userRegisterRepository.findUserRegisterByRegisterStatusAndRwId("entri", userProfile.getRw()==null ? null : Integer.parseInt(userProfile.getRw()));
		} else if (user.getRole().equals("ROLE_SUPER_ADMIN")) {
			userRegister = userRegisterRepository.findUserRegisterByRegisterStatus("entri");
		} else {
			userRegister = userRegisterRepository.findUserRegisterByRegisterStatusAndClusterId("entri", userProfile.getCluster());
		}

		List<UserRegisterResponse> response = new ArrayList<UserRegisterResponse>();
		
		for (UserRegister userreg : userRegister) {
			UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
			BeanUtils.copyProperties(userreg, userRegisterResponse);
			userRegisterResponse.setName(userreg.getFirstName() + " " + userreg.getLastName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			userRegisterResponse.setDateRegister(sdf.format(userreg.getDateAdd()));
			userRegisterResponse.setAddress(userreg.getClusterId() + 
												" " + 
												StringManipulation.isNull(userreg.getBlokId(),"") + 
												" " + 
												userreg.getBlokNumber() + 
												StringManipulation.isNull(userreg.getBlokIdentity(), "") );
			
			userRegisterResponse.setRt((userreg.getRtId() == null)? null : userreg.getRtId().toString());
			userRegisterResponse.setRw((userreg.getRtId() == null)? null : userreg.getRwId().toString());
			
			response.add(userRegisterResponse);
		}
		
		return response;
		
	}
	
	private String[] parseHomeNumber(String input) {
		String[] retval = new String[2];
		
		String a = input;
		Integer inputLength = input.length();
		int i = 0;
		for(i = 0; i < a.length(); i++){
		    char c = a.charAt(i);
		    if(c < '0'  || c > '9' )
		        break;
		}
		String numberPart  = a.substring(0, i);
		String alphaPart = "";
		if (i >= inputLength - 1 && inputLength > 1 ) 
		{
			alphaPart = a.substring(i);
		}
		
		retval[0] = numberPart;
		retval[1] = alphaPart;
		
		return retval;
	}

	@Override
	public List<ListKeluargaResponse> findFamilyByUser(String username) {
		// TODO Auto-generated method stub

		Optional<List<Family>> cekFamily = familyRepository.findByLeftJoinFamilyUser();

		if (cekFamily.isPresent()) {
			List<ListKeluargaResponse> listKeluarga = new ArrayList<ListKeluargaResponse>();
			for (Family family : cekFamily.get()) {
				ListKeluargaResponse item = new ListKeluargaResponse();
				BeanUtils.copyProperties(family, item);
				String address = family.getCluster().toUpperCase() + " " + family.getBlok().toUpperCase() + " "
						+ family.getNomor().toUpperCase() + " / RT" + family.getRt() + " RW" + family.getRw();
				item.setAddress(address);
				item.setFamilyId(item.getId().toString());
				listKeluarga.add(item);
			}
			return listKeluarga;
		}

		return null;
	}


}
