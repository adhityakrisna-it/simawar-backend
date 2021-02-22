package id.seringiskering.simawar.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import id.seringiskering.simawar.entity.User;
import id.seringiskering.simawar.exception.domain.DataNotFoundException;
import id.seringiskering.simawar.exception.domain.EmailExistException;
import id.seringiskering.simawar.exception.domain.EmailNotFoundException;
import id.seringiskering.simawar.exception.domain.NotAnImageFileException;
import id.seringiskering.simawar.exception.domain.UserNotFoundException;
import id.seringiskering.simawar.exception.domain.UsernameExistException;
import id.seringiskering.simawar.response.user.UserResponse;
import id.seringiskering.simawar.response.warga.ListKeluargaResponse;


public interface UserService {
	User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, JsonProcessingException;
	
	List<User> getUsers();
	
	List<UserResponse> getUsersForEditing(String username) throws DataNotFoundException, JsonMappingException, JsonProcessingException;
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
    
    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
    
    User updateUser(String username, 
    		        String newFirstName, 
    		        String newLastName, 
    		        String newEmail, 
    		        String clusterId, 
    				String blokId, 
    				String blokNumber, 
    				String blokIdentity) throws UserNotFoundException, EmailExistException, JsonProcessingException;
    
    void deleteUser(String username);
    
    void resetPassword(String email) throws EmailNotFoundException, MessagingException;
    
    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
    
    User updateUserRole(String username, String role);
    
    void updateUser(String username,
    				String editedUsername,
    				String newFirstName, 
    				String newLastName, 
    				String newEmail, 
    				String role, 
    				boolean isNonLocked, 
    				boolean isActive, 
    				String newPassword, String clusterId,
    				String blokId, 
    				String blokNumber, 
    				String blokIdentity,
    				String dataRw,
    				String dataRt,
    				String rw,
    				String rt,
    				String familyId) 
    throws UserNotFoundException, EmailExistException, JsonProcessingException;
    
    List<ListKeluargaResponse> findFamilyByUser(String username);
    

}
