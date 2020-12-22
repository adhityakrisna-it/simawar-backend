package id.seringiskering.simawar.controller;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import id.seringiskering.simawar.constant.FileConstant;
import id.seringiskering.simawar.domain.HttpResponse;
import id.seringiskering.simawar.domain.UserPrincipal;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.EmailNotFoundException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.exception.domain.UserNotFoundException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;
import id.seringiskering.simawar.profile.UserProfile;
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
	public ResponseEntity<User> login(@RequestBody User user) {
		authenticate(user.getUsername(), user.getPassword());
		User loginUser = userService.findUserByUsername(user.getUsername());
		UserPrincipal userPrincipal = new UserPrincipal(loginUser);
		HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
		return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
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

	@GetMapping("/list")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userService.getUsers();
		return new ResponseEntity<> (users, HttpStatus.OK);
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
	public ResponseEntity<User> updateProfileImage(
			@RequestParam("username") String username,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		User user = userService.updateProfileImage(username, profileImage);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}	
	
	@GetMapping(path = "/image/{username}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName ) throws IOException
	{
		return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + fileName));
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
