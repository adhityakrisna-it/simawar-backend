package id.seringiskering.simawar.controller;

import static id.seringiskering.simawar.constant.FileConstant.DOT;
import static id.seringiskering.simawar.constant.FileConstant.JPG_EXTENSION;
import static id.seringiskering.simawar.constant.SecurityConstant.JWT_TOKEN_HEADER;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.seringiskering.simawar.constant.FileConstant;
import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.domain.UserPrincipal;
import id.seringiskering.simawar.entity.FamilyUserOwner;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.EmailNotFoundException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.exception.domain.UserNotFoundException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.profile.UserProfile;
import id.seringiskering.simawar.request.user.AdminUpdateUserRequest;
import id.seringiskering.simawar.request.user.UpdateUserRequest;
import id.seringiskering.simawar.response.user.UserResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;
import id.seringiskering.simawar.service.UserService;
import id.seringiskering.simawar.utility.JWTTokenProvider;

@RestController
@RequestMapping(path = { "/", "/user" })
public class UserController {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
	private static final String EMAIL_SENT = "Email with new password has been sent to ";
	private UserService userService;
	private AuthenticationManager authenticationManager;
	private JWTTokenProvider jwtTokenProvider;
	
	private JwtAuthorizationFilter jwtAuthorizationFilter;

	@Autowired
	public UserController(UserService userService, AuthenticationManager authenticationManager,
			JWTTokenProvider jwtTokenProvider, JwtAuthorizationFilter jwtAuthorizationFilter) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
	}

	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@RequestBody User user) throws JsonMappingException, JsonProcessingException {
		authenticate(user.getUsername(), user.getPassword());
		User loginUser = userService.findUserByUsername(user.getUsername());
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(loginUser, userResponse);
		
		if (loginUser.getFamilyUserOwners()!=null) {
			for (FamilyUserOwner owner : loginUser.getFamilyUserOwners()) {
				userResponse.setFamilyId(owner.getId().getId().toString());
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		UserProfile userProfile = mapper.readValue(loginUser.getUserDataProfile(), UserProfile.class);
		userResponse.setUserDataProfile(userProfile);
		
		UserPrincipal userPrincipal = new UserPrincipal(loginUser);
		HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
		
		return new ResponseEntity<>(userResponse, jwtHeader, HttpStatus.OK);
		
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user)
			throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, JsonProcessingException {
		User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(),
				user.getEmail());
		return new ResponseEntity<>(newUser, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<User> add(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("username") String username,
			@RequestParam("email") String email, @RequestParam("role") String role,
			@RequestParam("isActive") String isActive, @RequestParam("isNonLocked") String isNonLocked,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		User newUser = userService.addNewUser(firstName, lastName, username, email, role,
				Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
		return new ResponseEntity<>(newUser, HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUsername,
			@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("role") String role, @RequestParam("isActive") String isActive,
			@RequestParam("isNonLocked") String isNonLocked,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		User updatedUser = userService.updateUser(currentUsername, firstName, lastName, username, email, role,
				Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	@PostMapping("/updateProfile")
	public ResponseEntity<UserResponse> updateProfile(@RequestBody UpdateUserRequest request) 
			throws JsonProcessingException, UserNotFoundException, EmailExistException
	{
		String username = jwtAuthorizationFilter.getValidUsername();
		User userSave = userService.updateUser(username, 
												request.getFirstName(), 
												request.getLastName(), 
												request.getEmail(), 
												request.getClusterId(), 
												request.getBlokId(), 
												request.getBlokNumber(), 
												request.getBlokIdentity()
												);
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(userSave, userResponse);
		
		ObjectMapper mapper = new ObjectMapper();
		UserProfile userProfile = mapper.readValue(userSave.getUserDataProfile(), UserProfile.class);
		userResponse.setUserDataProfile(userProfile);
		
		return new ResponseEntity<>(userResponse, HttpStatus.OK);

	}
	
	@GetMapping("/find/{username}")
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {
		User user = userService.findUserByUsername(username);
		
		/**
		 *
		ObjectMapper mapper = new ObjectMapper();
		try {
			UserProfile userProfile = mapper.readValue(user.getUserDataProfile(), UserProfile.class);
			System.out.println(userProfile.getRt() + "/" + userProfile.getRw());
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		 */
		
		LOGGER.info("USER REQUEST JWT :" + jwtAuthorizationFilter.getValidUsername());
		
		return new ResponseEntity<> (user, HttpStatus.OK);
	}
	
	@GetMapping("/findMyProfile")
	public ResponseEntity<User> getMyUser() {
		String username = jwtAuthorizationFilter.getValidUsername();
		User user = userService.findUserByUsername(username);
		return new ResponseEntity<> (user, HttpStatus.OK);
	}

	@GetMapping("/list")
	@PreAuthorize("hasAnyAuthority('user:update')")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userService.getUsers();
		return new ResponseEntity<> (users, HttpStatus.OK);
	}
	
	@GetMapping("/findFamilyByUser")
	@PreAuthorize("hasAnyAuthority('user:update')")
	public ResponseEntity<List<ListKeluargaResponse>> findFamilyByUser() {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<ListKeluargaResponse> response = userService.findFamilyByUser(username);
		return new ResponseEntity<> (response, HttpStatus.OK);
	}

	@GetMapping("/resetPassword/{email}")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws EmailNotFoundException, MessagingException {
		userService.resetPassword(email);
		return response(HttpStatus.OK, EMAIL_SENT + email);
	}
	
	@DeleteMapping("/delete/{username}")
	@PreAuthorize("hasAnyAuthority('user:delete')") // ssesuai setting pada global configuration di class SecurityConfiguration, di situ sudah disetting ==> @EnableGlobalMethodSecurity(prePostEnabled = true) 
	public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username")String username) throws IOException {
		userService.deleteUser(username);
		Path userFolder = Paths.get(FileConstant.USER_FOLDER + username).toAbsolutePath().normalize();
		FileUtils.deleteDirectory(new File(userFolder.toString()));
		return response(HttpStatus.OK, USER_DELETED_SUCCESSFULLY);
		//return response(HttpStatus.NO_CONTENT, USER_DELETED_SUCCESSFULLY);
	}
	
	@PostMapping("/updateProfileImage")
	public ResponseEntity<UserResponse> updateProfileImage(
			@RequestParam("username") String username,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		User user = userService.updateProfileImage(username, profileImage);
		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(user, userResponse);
		
		ObjectMapper mapper = new ObjectMapper();
		UserProfile userProfile = mapper.readValue(user.getUserDataProfile(), UserProfile.class);
		userResponse.setUserDataProfile(userProfile);
		
		UserPrincipal userPrincipal = new UserPrincipal(user);
		
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}	
	
	@GetMapping(path = "/image/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfileImage(@PathVariable("username") String username) throws IOException
	{
		LOGGER.info("Path File : " + FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + username + DOT + JPG_EXTENSION);
		return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + username + DOT + JPG_EXTENSION));
	}

	@GetMapping(path = "/image/profile/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException
	{
		URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (InputStream inputStream = url.openStream() ) {
			int bytesRead;
			byte[] chunk = new byte[1024];
			while((bytesRead = inputStream.read(chunk)) > 0) 
			{
				byteArrayOutputStream.write(chunk, 0 , bytesRead);
			}
			return byteArrayOutputStream.toByteArray();
		}
	}
	
	@PostMapping("/updateUserRole/{username}/{role}")
	public ResponseEntity<User> updateUserRole(@PathVariable("username")String username, @PathVariable("role") String role) {
		User user = userService.updateUserRole(username, role);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/findUsersForEditing")
	@PreAuthorize("hasAnyAuthority('user:update')")
	public ResponseEntity<List<UserResponse>> getUsersForEditing() 
			throws DataNotFoundException, JsonMappingException, JsonProcessingException {
		String username = jwtAuthorizationFilter.getValidUsername();
		List<UserResponse> userResponse = userService.getUsersForEditing(username);
		
		return new ResponseEntity<> (userResponse, HttpStatus.OK);
	}	
	
	@PostMapping("/updateUser")
	@PreAuthorize("hasAnyAuthority('user:update')") // ssesuai setting pada global configuration di class SecurityConfiguration, di situ sudah disetting ==> @EnableGlobalMethodSecurity(prePostEnabled = true) 
	public ResponseEntity<HttpResponse> updateUser(@RequestBody AdminUpdateUserRequest updateRequest) 
			throws JsonProcessingException, UserNotFoundException, EmailExistException  {
		String username = jwtAuthorizationFilter.getValidUsername();
		
		userService.updateUser(
				username, 
				updateRequest.getUsername(), 
				updateRequest.getFirstName(), 
				updateRequest.getLastName(), 
				updateRequest.getEmail(), 
				updateRequest.getRole(), 
				Boolean.parseBoolean(updateRequest.getIsNotLocked()) , 
				Boolean.parseBoolean(updateRequest.getIsActive()), 
				updateRequest.getPassword(), 
				updateRequest.getClusterId(), 
				updateRequest.getBlokId(), 
				updateRequest.getBlokNumber(), 
				updateRequest.getBlokIdentity(),
				updateRequest.getDataRw(),
				updateRequest.getDataRt(),
				updateRequest.getRw(),
				updateRequest.getRt(),
				updateRequest.getFamilyId());
		
		return response(HttpStatus.OK, "User berhasil diupdate");
		
	}

	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		// TODO Auto-generated method stub
		return new ResponseEntity<> (new HttpResponse(httpStatus.value(),httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
	}
	
	
	private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
		// TODO Auto-generated method stub
		HttpHeaders headers = new HttpHeaders();
		headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));

		return headers;
	}

	private void authenticate(String username, String password) {
		// TODO Auto-generated method stub
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

}
