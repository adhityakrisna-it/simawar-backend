package id.seringiskering.simawar.response.warga;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListWargaResponse {
	private Long id;
	private String familyStatus;
	private String name;
	private String phoneNumber1;
	private String address;	
	private Date birthDate;
	private String sex;
	private String ktpUrl;
	private String kkUrl;
	private String profileUrl;
}
