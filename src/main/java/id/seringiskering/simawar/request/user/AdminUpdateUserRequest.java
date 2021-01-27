package id.seringiskering.simawar.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateUserRequest {
	String username;
	String email;
	String firstName;
	String lastName;
	String clusterId;
	String blokId;
	String blokNumber;
	String blokIdentity;
	String password;
	String isActive;
	String isNotLocked;
	String role;
	String dataRw;
	String dataRt;
	String rt;
	String rw;
}
