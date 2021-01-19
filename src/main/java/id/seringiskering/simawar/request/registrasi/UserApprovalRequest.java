package id.seringiskering.simawar.request.registrasi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserApprovalRequest {

	private Long id;
	private String role;
	private String familyId;
	private String persilId;
	private String blok;
	private String nomor;
	private String clusterId;
	private String blokId;
	private String blokNumber;
	private String blokIdentity;
}
