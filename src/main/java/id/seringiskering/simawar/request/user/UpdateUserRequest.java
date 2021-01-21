package id.seringiskering.simawar.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
	String username;
	String email;
	String firstName;
	String lastName;
	String clusterId;
	String blokId;
	String blokNumber;
	String blokIdentity;
}
