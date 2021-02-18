package id.seringiskering.simawar.response.warga;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoWargaResponse {
	private Long id;
	private String name;
	private String familyStatus;
	private String sex;
	private String religion;
	private Date birthDate;
	private String phoneNumber1;
	private String phoneNumber2;
	private String email;	
	private String profileUrl;
}
