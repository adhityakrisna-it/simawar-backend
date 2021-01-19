package id.seringiskering.simawar.request.registrasi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {

	private String firstName;
	private String lastName; 
	private String username; 
	private String email; 
	private String clusterId;
	private String blokId;
	private int blokNumber; 
	private String blokIdentity;
	private String password;
	private String homeNumber;
	
}
