package id.seringiskering.simawar.request.registrasi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserApprovalRequest {

	private Long id;
	private String role;
	private String familyId;		
	
}
