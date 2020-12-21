package id.seringiskering.simawar.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {

	private String username;
	private String firstName;
	private String lastName;
	private String blok_id;
	private String cluster_id;
	private String blok_number;
	
}
