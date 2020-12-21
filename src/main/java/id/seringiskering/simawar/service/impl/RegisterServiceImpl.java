package id.seringiskering.simawar.service.impl;

import static id.seringiskering.simawar.constant.UserImplConstant.EMAIL_ALREADY_EXIST;
import static id.seringiskering.simawar.constant.UserImplConstant.USERNAME_ALREADY_EXIST;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import id.seringiskering.simawar.entity.Persil;
import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.entity.UserRegister;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.repository.PersilRepository;
import id.seringiskering.simawar.repository.UserRegisterRepository;
import id.seringiskering.simawar.repository.UserRepository;
import id.seringiskering.simawar.request.registrasi.UserRegistrationRequest;
import id.seringiskering.simawar.service.RegisterService;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

	private static final String EMAIL_EXIST_REGISTER = "Email ini sedang dalam proses approval";

	private static final String USERNAME_EXIST_REGISTER = "Username ini sedang dalam proses approval";

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	private UserRegisterRepository userRegisterRepository;
	private UserRepository userRepository;
	private PersilRepository persilRepository;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public RegisterServiceImpl(UserRegisterRepository userRegisterRepository, 
							   BCryptPasswordEncoder passwordEncoder,
							   UserRepository userRepository, 
							   PersilRepository persilRepository) {
		this.userRegisterRepository = userRegisterRepository;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.persilRepository = persilRepository;
	}

	@Override
	public void registerNewUser(UserRegistrationRequest request) throws UsernameExistException, EmailExistException {
		
		// TODO Auto-generated method stub		
		validateEntriNewUsernameAndEmail(request.getUsername(), request.getEmail());
		
		UserRegister userRegister = new UserRegister();
		userRegister.setFirstName(request.getFirstName());
		userRegister.setLastName(request.getLastName());
		userRegister.setUsername(request.getUsername());
		userRegister.setEmail(request.getEmail());
		userRegister.setClusterId(request.getClusterId());
		userRegister.setBlokNumber(request.getBlokNumber());
		userRegister.setBlokIdentity(request.getBlokIdentity());
		userRegister.setPassword(encodePassword(request.getPassword()));
		
		List<Persil> persil = persilRepository.findPersilByClusterIdAndBlokIdAndBlokNumberAndBlokIdentity
											(request.getClusterId(), 
													request.getBlokId(), 
													request.getBlokNumber(), 
													request.getBlokIdentity());
		
		if (persil.size() > 0) {
			Persil p = persil.get(0);
			userRegister.setRtId(p.getRtId());
			userRegister.setRwId(p.getRwId());
			userRegister.setPersilId(p.getPersilId());
		}
		
		userRegisterRepository.save(userRegister);

	}
	

	@Override
	public List<UserRegister> findUserRegisterForApprove(String username) {
		// TODO Auto-generated method stub
		
		validateRoleApproval(username);
		
		
		
		return null;
	}
	
	private void validateRoleApproval(String username) {
		// TODO Auto-generated method stub
		
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


}
