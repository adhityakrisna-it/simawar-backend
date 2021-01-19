package id.seringiskering.simawar.response.register;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterResponse {

		private String firstName;
		private String lastName;
		private String email;
		private String username;
		private String clusterId;
		private String blokId;
		private int blokNumber;
		private String blokIdentity;
		private Long id;
		private String name;
		private String registerStatus;
		private String dateRegister;
		private String address;
}
