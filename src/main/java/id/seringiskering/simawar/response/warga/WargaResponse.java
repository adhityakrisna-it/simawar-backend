package id.seringiskering.simawar.response.warga;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WargaResponse {
	private Long id;
	private String addressAsId;
	private Date dateAdd;
	private Date dateUpdate;
	private String familyStatus;
	private String kependudukanStatus;
	private String kkUrl;
	private String ktpUrl;
	private String name;
	private String noKk;
	private String noKtp;
	private String phoneNumber1;
	private String phoneNumber2;
	private String phoneNumber3;
	private String profileUrl;
	private String religion;
	private String sex;
	private String work;
	private String address;	
	private Date birthDate;
	private String note;
	private String bpjsNo;
	private String kisNo;
	private String bloodType;
	private String lastEducation;
	private String email;
}
