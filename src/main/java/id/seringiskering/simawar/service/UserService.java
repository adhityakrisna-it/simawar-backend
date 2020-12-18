package id.seringiskering.simawar.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.EmailNotFoundException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.exception.domain.UserNotFoundException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;


public interface UserService {
	User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;
	
	List<User> getUsers();
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
    
    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
    
    void deleteUser(String username);
    
    void resetPassword(String email) throws EmailNotFoundException, MessagingException;
    
    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
    
    	
}
