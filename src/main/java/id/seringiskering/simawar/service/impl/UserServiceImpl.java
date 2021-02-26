package id.seringiskering.simawar.service.impl;

import static id.seringiskering.simawar.constant.FileConstant.DIRECTORY_CREATED;
import static id.seringiskering.simawar.constant.FileConstant.DOT;
import static id.seringiskering.simawar.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static id.seringiskering.simawar.constant.FileConstant.JPG_EXTENSION;
import static id.seringiskering.simawar.constant.FileConstant.USER_FOLDER;
import static id.seringiskering.simawar.constant.UserImplConstant.EMAIL_ALREADY_EXIST;
import static id.seringiskering.simawar.constant.UserImplConstant.FOUND_USER_FROM_USERNAME;
import static id.seringiskering.simawar.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;
import static id.seringiskering.simawar.constant.UserImplConstant.USERNAME_ALREADY_EXIST;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.seringiskering.simawar.constant.FileConstant;
import id.seringiskering.simawar.constant.UserImplConstant;
import id.seringiskering.simawar.domain.UserPrincipal;
import id.seringiskering.simawar.entity.Family;
import id.seringiskering.simawar.entity.FamilyMember;
import id.seringiskering.simawar.entity.FamilyMemberUserOwner;
import id.seringiskering.simawar.entity.FamilyMemberUserOwnerPK;
import id.seringiskering.simawar.entity.FamilyUserOwner;
import id.seringiskering.simawar.entity.FamilyUserOwnerPK;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.entity.UserPersil;
import id.seringiskering.simawar.enumeration.Role;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.EmailNotFoundException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.exception.domain.UserNotFoundException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.function.StringManipulation;
import id.seringiskering.simawar.profile.UserProfile;
import id.seringiskering.simawar.repository.FamilyMemberUserOwnerRepository;
import id.seringiskering.simawar.repository.FamilyRepository;
import id.seringiskering.simawar.repository.FamilyUserOwnerRepository;
import id.seringiskering.simawar.repository.UserPersilRepository;
import id.seringiskering.simawar.repository.UserRepository;
import id.seringiskering.simawar.response.user.UserResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.service.EmailService;
import id.seringiskering.simawar.service.LoginAttemptService;
import id.seringiskering.simawar.service.UserService;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

	private static final String DATA_USER_TIDAK_DITEMUKAN = "Data user tidak ditemukan";

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	private UserRepository userRepository;
	private UserPersilRepository userPersilRepository;
	private FamilyRepository familyRepository;
	private FamilyUserOwnerRepository familyUserOwnerRepository;
	private FamilyMemberUserOwnerRepository familyMemberUserOwnerRepository;

	private BCryptPasswordEncoder passwordEncoder;

	private LoginAttemptService loginAttemptService;

	private EmailService emailService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
			LoginAttemptService loginAttemptService, EmailService emailService,
			UserPersilRepository userPersilRepository, FamilyRepository familyRepository,
			FamilyUserOwnerRepository familyUserOwnerRepository,
			FamilyMemberUserOwnerRepository familyMemberUserOwnerRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;
		this.emailService = emailService;
		this.userPersilRepository = userPersilRepository;
		this.familyRepository = familyRepository;
		this.familyUserOwnerRepository = familyUserOwnerRepository;
		this.familyMemberUserOwnerRepository = familyMemberUserOwnerRepository; 
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByUsername(username);
		if (user == null) {
			LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
			throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		} else {
			validateLoginAttempt(user);
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			LOGGER.info(FOUND_USER_FROM_USERNAME + username);
			return userPrincipal;
		}
	}

	private void validateLoginAttempt(User user) {
		// TODO Auto-generated method stub
		if (user.isNotLocked()) {
			if (loginAttemptService.hasExceedMaxAttempts(user.getUsername())) {
				user.setNotLocked(false);
			} else {
				user.setNotLocked(true);
			}
		} else {
			loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
		}
	}

	@Override
	public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException,
			UsernameExistException, EmailExistException, MessagingException, JsonProcessingException {
		// TODO Auto-generated method stub
		validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
		User user = new User();
		user.setUserId(generateUserId());
		String password = generatePassword();
		String encodedPassword = encodePassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(encodedPassword);
		user.setActive(true);
		user.setNotLocked(true);

		if (username.equals("adhityakrisna") || username.equals("superadmin")) {
			user.setRole(Role.ROLE_SUPER_ADMIN.name());
			user.setAuthorities(Role.ROLE_SUPER_ADMIN.getAuthorities());

			UserProfile userProfile = new UserProfile();
			userProfile.setCluster("safir");
			userProfile.setRt("rt2");
			userProfile.setRw("rw21");
			userProfile.setKecamatan("tapos");
			userProfile.setKota("depok");

			ObjectMapper mapper = new ObjectMapper();
			String jsonProfile = mapper.writeValueAsString(userProfile);

			user.setUserDataProfile(jsonProfile);

		} else {
			user.setRole(Role.ROLE_USER.name());
			user.setAuthorities(Role.ROLE_USER.getAuthorities());
		}

		user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
		userRepository.save(user);
		LOGGER.info("New user password : " + password);
		emailService.sendNewPasswordEmail(firstName, password, email);
		return user;
	}

	@Override
	public User addNewUser(String firstName, String lastName, String username, String email, String role,
			boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException,
			UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		// TODO Auto-generated method stub
		validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
		User user = new User();
		String password = generatePassword();
		user.setUserId(generateUserId());
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(encodePassword(password));
		user.setActive(isActive);
		user.setNotLocked(isNonLocked);
		user.setRole(getRoleEnumName(role).name());
		user.setAuthorities(getRoleEnumName(role).getAuthorities());
		user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
		userRepository.save(user);
		saveProfileImage(user, profileImage);
		LOGGER.info("New user password : " + password);
		return user;
	}

	@Override
	public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername,
			String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
			NotAnImageFileException {
		User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
		currentUser.setFirstName(newFirstName);
		currentUser.setLastName(newLastName);
		currentUser.setUsername(newUsername);
		currentUser.setEmail(newEmail);
		currentUser.setActive(isActive);
		currentUser.setNotLocked(isNonLocked);
		currentUser.setRole(getRoleEnumName(role).name());
		currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
		userRepository.save(currentUser);
		saveProfileImage(currentUser, profileImage);
		return currentUser;
	}

	@Override
	public User updateUser(String username, String newFirstName, String newLastName, String newEmail, String clusterId,
			String blokId, String blokNumber, String blokIdentity)
			throws UserNotFoundException, EmailExistException, JsonProcessingException {
		// TODO Auto-generated method stub
		User currentUser = validateNewEmail(username, newEmail);
		currentUser.setFirstName(newFirstName);
		currentUser.setLastName(newLastName);
		currentUser.setEmail(newEmail);
		currentUser.setUserDataProfile(
				getUserProfile(currentUser.getRole(), null, null, null, clusterId, blokId, blokNumber, blokIdentity));
		Optional<UserPersil> cekUserPersil = userPersilRepository.findById(currentUser.getUserId());
		if (cekUserPersil.isEmpty()) {
			UserPersil userPersil = new UserPersil();
			userPersil.setUserId(currentUser.getUserId());
			userPersil.setCluster(clusterId);
			userPersil.setNomor(blokNumber);
			userPersil.setNomorTambahan(blokIdentity);
			userPersil.setBlok(blokId);
			userPersil.setUser(currentUser);
			currentUser.setUserPersil(userPersil);
		} else {
			UserPersil userPersil = cekUserPersil.get();
			userPersil.setUserId(currentUser.getUserId());
			userPersil.setCluster(clusterId);
			userPersil.setNomor(blokNumber);
			userPersil.setNomorTambahan(blokIdentity);
			userPersil.setBlok(blokId);
			userPersil.setUser(currentUser);
			currentUser.setUserPersil(userPersil);
		}
		userRepository.save(currentUser);

		return currentUser;

	}

	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		// userRepository.deleteById(id);
		User userDelete = userRepository.findUserByUsername(username);
		userRepository.deleteById(userDelete.getUserId());
	}

	@Override
	public void resetPassword(String email) throws EmailNotFoundException, MessagingException {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByEmail(email);
		if (user == null) {
			throw new EmailNotFoundException(UserImplConstant.NO_USER_FOUND_BY_EMAIL + email);
		}
		String password = generatePassword();
		user.setPassword(encodePassword(password));
		userRepository.save(user);
		emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());
	}

	@Override
	public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException,
			UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		// TODO Auto-generated method stub
		User user = validateNewUsernameAndEmail(username, null, null);
		saveProfileImage(user, profileImage);
		return user;
	}

	@Override
	public User updateUserRole(String username, String role) {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByUsername(username);
		user.setRole(getRoleEnumName(role).name());
		user.setAuthorities(getRoleEnumName(role).getAuthorities());
		userRepository.save(user);
		return user;
	}

	@Override
	public List<UserResponse> getUsersForEditing(String username)
			throws DataNotFoundException, JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByUsername(username);
		if (user.getRole().equals("ROLE_PENGURUS_RT") && user.getUserDataProfile() != null) {
			ObjectMapper mapper = new ObjectMapper();
			UserProfile userprofile = mapper.readValue(user.getUserDataProfile(), UserProfile.class);

			Optional<List<User>> listUser = userRepository
					.queryByRt(StringManipulation.isNull(userprofile.getRt(), " "));

			if (!listUser.isPresent()) {
				throw new DataNotFoundException(DATA_USER_TIDAK_DITEMUKAN);
			}

			List<UserResponse> userResponse = new ArrayList<UserResponse>();

			if (listUser.get().size() > 0) {
				for (User userlist : listUser.get()) {
					UserResponse usercopy = new UserResponse();
					BeanUtils.copyProperties(userlist, usercopy);

					ObjectMapper mapper3 = new ObjectMapper();
					UserProfile userProfile = mapper3.readValue(userlist.getUserDataProfile(), UserProfile.class);
					usercopy.setUserDataProfile(userProfile);

					if (userlist.getFamilyUserOwners() != null) {
						Set<FamilyUserOwner> listfamilyuserowner = userlist.getFamilyUserOwners();
						for (FamilyUserOwner owner : listfamilyuserowner) {
							usercopy.setFamilyId(owner.getId().getId().toString());
						}
					}

					userResponse.add(usercopy);

				}
			}

			return userResponse;

		} else if (user.getRole().equals("ROLE_PENGURUS_RW") && user.getUserDataProfile() != null) {
			ObjectMapper mapper = new ObjectMapper();
			UserProfile userprofile = mapper.readValue(user.getUserDataProfile(), UserProfile.class);

			Optional<List<User>> listUser = userRepository.queryByRw(userprofile.getRw());

			if (!listUser.isPresent()) {
				throw new DataNotFoundException(DATA_USER_TIDAK_DITEMUKAN);
			}

			List<UserResponse> userResponse = new ArrayList<UserResponse>();

			if (listUser.get().size() > 0) {
				for (User userlist : listUser.get()) {
					UserResponse usercopy = new UserResponse();
					BeanUtils.copyProperties(userlist, usercopy);

					ObjectMapper mapper2 = new ObjectMapper();
					UserProfile userProfile = mapper2.readValue(userlist.getUserDataProfile(), UserProfile.class);
					usercopy.setUserDataProfile(userProfile);

					if (userlist.getFamilyUserOwners() != null) {
						Set<FamilyUserOwner> listfamilyuserowner = userlist.getFamilyUserOwners();
						for (FamilyUserOwner owner : listfamilyuserowner) {
							usercopy.setFamilyId(owner.getId().getId().toString());
						}
					}

					userResponse.add(usercopy);

				}
			}

			return userResponse;

		} else if (user.getRole().equals("ROLE_SUPER_ADMIN")) {
			List<User> listUser = userRepository.findAll();
			List<UserResponse> userResponse = new ArrayList<UserResponse>();

			if (listUser.size() > 0) {
				for (User userlist : listUser) {
					UserResponse usercopy = new UserResponse();
					BeanUtils.copyProperties(userlist, usercopy);

					ObjectMapper mapper = new ObjectMapper();
					if (userlist.getUserDataProfile() != null) {
						UserProfile userProfile = mapper.readValue(userlist.getUserDataProfile(), UserProfile.class);
						usercopy.setUserDataProfile(userProfile);

						if (userlist.getFamilyUserOwners() != null) {
							// List<FamilyUserOwner> listfamilyuserowner = (List<FamilyUserOwner>)
							// userlist.getFamilyUserOwners();
							Set<FamilyUserOwner> listfamilyuserowner = userlist.getFamilyUserOwners();
							for (FamilyUserOwner owner : listfamilyuserowner) {
								usercopy.setFamilyId(owner.getId().getId().toString());
							}
						}

					}
					userResponse.add(usercopy);
				}
			}

			return userResponse;

		}

		return null;
	}

	private String getTemporaryProfileImageUrl(String username) {
		// TODO Auto-generated method stub
		// return
		// ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.DEFAULT_USER_IMAGE_PATH
		// + username).toUriString();
		return FileConstant.DEFAULT_USER_IMAGE_PATH + username;
	}

	private String encodePassword(String password) {
		// TODO Auto-generated method stub
		return passwordEncoder.encode(password);
	}

	private String generatePassword() {
		// TODO Auto-generated method stub
		return RandomStringUtils.randomAlphanumeric(10);
	}

	private String generateUserId() {
		// TODO Auto-generated method stub
		return RandomStringUtils.randomNumeric(10);
	}

	private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
			throws UserNotFoundException, UsernameExistException, EmailExistException {
		// TODO Auto-generated method stub
		User userByNewUsername = findUserByUsername(newUsername);
		User userByNewEmail = findUserByEmail(newEmail);

		if (StringUtils.isNotBlank(currentUsername)) {
			User currentUser = findUserByUsername(currentUsername);
			if (currentUser == null) {
				throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
			}
			if (userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
				throw new UsernameExistException(USERNAME_ALREADY_EXIST);
			}
			if (userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
				throw new EmailExistException(EMAIL_ALREADY_EXIST);
			}
			return currentUser;

		} else {
			if (userByNewUsername != null) {
				throw new UsernameExistException(USERNAME_ALREADY_EXIST);
			}
			if (userByNewEmail != null) {
				throw new EmailExistException(EMAIL_ALREADY_EXIST);
			}
			return null;
		}

	}

	private User validateNewEmail(String username, String newEmail) throws UserNotFoundException, EmailExistException {
		User userByNewEmail = findUserByEmail(newEmail);
		User currentUser = findUserByUsername(username);
		if (currentUser == null) {
			throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		}
		if (userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
			throw new EmailExistException(EMAIL_ALREADY_EXIST);
		}
		return currentUser;

	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findUserByUsername(username);
	}

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findUserByEmail(email);
	}

	@Override
	public void updateUser(String username, String editedUsername, String newFirstName, String newLastName,
			String newEmail, String role, boolean isNonLocked, boolean isActive, String newPassword, String clusterId,
			String blokId, String blokNumber, String blokIdentity, String dataRw, String dataRt, String rw, String rt,
			String familyId) throws UserNotFoundException, EmailExistException, JsonProcessingException {
		// TODO Auto-generated method stub

		User currentUser = validateNewEmail(editedUsername, newEmail);
		currentUser.setRole(role);
		currentUser.setFirstName(newFirstName);
		currentUser.setLastName(newLastName);
		currentUser.setEmail(newEmail);
		currentUser.setActive(isActive);
		currentUser.setNotLocked(isNonLocked);
		currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());

		Integer iRt = (dataRt == null) ? null : Integer.valueOf(dataRt);
		Integer iRw = (dataRw == null) ? null : Integer.valueOf(dataRw);

		currentUser.setUserDataProfile(
				getUserProfile(currentUser.getRole(), iRt, iRw, null, clusterId, blokId, blokNumber, blokIdentity));

		if (!newPassword.equals("")) {
			currentUser.setPassword(encodePassword(newPassword));
		}

		Optional<UserPersil> cekUserPersil = userPersilRepository.findById(currentUser.getUserId());
		if (cekUserPersil.isEmpty()) {
			UserPersil userPersil = new UserPersil();
			userPersil.setUserId(currentUser.getUserId());
			userPersil.setCluster(clusterId);
			userPersil.setNomor(blokNumber);
			userPersil.setNomorTambahan(blokIdentity);
			userPersil.setBlok(blokId);
			userPersil.setUser(currentUser);
			userPersil.setRt(rt);
			userPersil.setRw(rw);
			currentUser.setUserPersil(userPersil);
		} else {
			UserPersil userPersil = cekUserPersil.get();
			userPersil.setUserId(currentUser.getUserId());
			userPersil.setCluster(clusterId);
			userPersil.setNomor(blokNumber);
			userPersil.setNomorTambahan(blokIdentity);
			userPersil.setBlok(blokId);
			userPersil.setUser(currentUser);
			userPersil.setRt(rt);
			userPersil.setRw(rw);
			currentUser.setUserPersil(userPersil);
		}
		userRepository.save(currentUser);

		Optional<List<FamilyUserOwner>> cekfamilyowner = familyUserOwnerRepository
				.findByIdUserId(currentUser.getUserId());
		if (cekfamilyowner.isPresent()) {
			for (FamilyUserOwner familyOwner : cekfamilyowner.get()) {
				familyUserOwnerRepository.delete(familyOwner);
			}
		}
		
		Optional<List<FamilyMemberUserOwner>> cekmemberowner = familyMemberUserOwnerRepository.findByIdUserId(currentUser.getUserId());
		if (cekmemberowner.isPresent()) {
			for (FamilyMemberUserOwner owner: cekmemberowner.get()) {
				familyMemberUserOwnerRepository.delete(owner);
			}
		}

		if (familyId != null) {
			FamilyUserOwnerPK idowner = new FamilyUserOwnerPK();
			idowner.setId(Long.parseLong(familyId));
			idowner.setUserId(currentUser.getUserId());
			FamilyUserOwner familyOwner = new FamilyUserOwner();
			familyOwner.setId(idowner);
			familyUserOwnerRepository.save(familyOwner);
			
        	Optional<Family> cekfamily = familyRepository.findById(Long.parseLong(familyId));
        	if (cekfamily.isPresent()) {
        		if (cekfamily.get().getFamilyMembers() != null) {
        			for (FamilyMember familymember: cekfamily.get().getFamilyMembers()) {
        				
        				FamilyMemberUserOwnerPK pk = new FamilyMemberUserOwnerPK();
        				pk.setId(familymember.getId());
        				pk.setUserId(currentUser.getUserId());
        				
        				FamilyMemberUserOwner familymemberowner = new FamilyMemberUserOwner();
        				familymemberowner.setId(pk);
        				familyMemberUserOwnerRepository.save(familymemberowner);
        			}
        		}
        	}			
		}

		return;
	}

	@Override
	public List<ListKeluargaResponse> findFamilyByUser(String username) {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByUsername(username);
		List<Family> cekFamily = familyRepository.findAll();

		if (cekFamily.size()>0) {
			List<ListKeluargaResponse> listKeluarga = new ArrayList<ListKeluargaResponse>();
			for (Family family : cekFamily) {
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
	
	private List<ListKeluargaResponse> findFamilyByUser2(String username) {
		// TODO Auto-generated method stub
		User user = userRepository.findUserByUsername(username);
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

			FamilyUserOwnerPK id = new FamilyUserOwnerPK();
			id.setUserId(user.getUserId());
			Optional<FamilyUserOwner> cekfamily2 = familyUserOwnerRepository.findById(id);
			if (cekfamily2.isPresent()) {
				
				Family family = cekfamily2.get().getFamily();
				
				ListKeluargaResponse item2 = new ListKeluargaResponse();
				BeanUtils.copyProperties(family, item2);
				String address = family.getCluster().toUpperCase() + " " + family.getBlok().toUpperCase() + " "
						+ family.getNomor().toUpperCase() + " / RT" + family.getRt() + " RW" + family.getRw();
				item2.setAddress(address);
				item2.setFamilyId(item2.getId().toString());
				listKeluarga.add(item2);

			}

			return listKeluarga;
		}

		return null;
	}

	private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
		// TODO Auto-generated method stub
		if (profileImage != null) {

			if (!Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE)
					.contains(profileImage.getContentType())) {
				throw new NotAnImageFileException(profileImage.getOriginalFilename() + " is not an image file");
			}

			Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
			if (!Files.exists(userFolder)) {
				Files.createDirectories(userFolder);
				LOGGER.info(DIRECTORY_CREATED + userFolder);
			}
			Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
			Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION),
					StandardCopyOption.REPLACE_EXISTING);
			user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
			LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM);
		}
	}

	private String setProfileImageUrl(String username) {
		// TODO Auto-generated method stub
		// return ServletUriComponentsBuilder.fromCurrentContextPath().path(
		// USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT +
		// JPG_EXTENSION).toUriString();
		return FileConstant.USER_IMAGE_PATH + username;
	}

	private Role getRoleEnumName(String role) {
		// TODO Auto-generated method stub
		return Role.valueOf(role.toUpperCase());
	}

	private String getUserProfile(String role, Integer rtId, Integer rwId, String kelurahanId, String clusterId,
			String blok, String nomor, String nomorTambahan) throws JsonProcessingException {

		UserProfile userProfile = new UserProfile();

		if (role.equals("ROLE_USER")) {
			userProfile.setCluster(clusterId);
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
			userProfile.setRt((rtId == null) ? null : rtId.toString());
		} else if (role.equals("ROLE_PENGURUS_RW")) {
			userProfile.setCluster(clusterId);
			userProfile.setRw((rwId == null) ? null : rwId.toString());
		} else {
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

}
