package id.seringiskering.simawar.request.warga;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

public class SaveWargaRequest {
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
	
	public SaveWargaRequest(Long id, String addressAsId, Date dateAdd, Date dateUpdate, String familyStatus,
			String kependudukanStatus, String kkUrl, String ktpUrl, String name, String noKk, String noKtp,
			String phoneNumber1, String phoneNumber2, String phoneNumber3, String profileUrl, String religion,
			String sex, String work, String address, Date birthDate, String note, String bpjsNo, String kisNo,
			String bloodType, String lastEducation) {
		super();
		this.id = id;
		this.addressAsId = addressAsId;
		this.dateAdd = dateAdd;
		this.dateUpdate = dateUpdate;
		this.familyStatus = familyStatus;
		this.kependudukanStatus = kependudukanStatus;
		this.kkUrl = kkUrl;
		this.ktpUrl = ktpUrl;
		this.name = name;
		this.noKk = noKk;
		this.noKtp = noKtp;
		this.phoneNumber1 = phoneNumber1;
		this.phoneNumber2 = phoneNumber2;
		this.phoneNumber3 = phoneNumber3;
		this.profileUrl = profileUrl;
		this.religion = religion;
		this.sex = sex;
		this.work = work;
		this.address = address;
		this.birthDate = birthDate;
		this.note = note;
		this.bpjsNo = bpjsNo;
		this.kisNo = kisNo;
		this.bloodType = bloodType;
		this.lastEducation = lastEducation;
	}

	public SaveWargaRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddressAsId() {
		return addressAsId;
	}

	public void setAddressAsId(String addressAsId) {
//		if (!addressAsId.equals("null")) {
//			this.addressAsId = addressAsId;
//		}
		this.addressAsId = addressAsId.equals("null") ? null : addressAsId;
	}

	public Date getDateAdd() {
		return dateAdd;
	}

	public void setDateAdd(Date dateAdd) {
		this.dateAdd = dateAdd;
	}

	public Date getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public String getFamilyStatus() {
		return familyStatus;
	}

	public void setFamilyStatus(String familyStatus) {
		this.familyStatus = familyStatus;
	}

	public String getKependudukanStatus() {
		return kependudukanStatus;
	}

	public void setKependudukanStatus(String kependudukanStatus) {
		this.kependudukanStatus = kependudukanStatus;
	}

	public String getKkUrl() {
		return kkUrl;
	}

	public void setKkUrl(String kkUrl) {
		this.kkUrl = kkUrl;
	}

	public String getKtpUrl() {
		return ktpUrl;
	}

	public void setKtpUrl(String ktpUrl) {
		this.ktpUrl = ktpUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.equals("null") ? null : name;
	}

	public String getNoKk() {
		return noKk;
	}

	public void setNoKk(String noKk) {
		this.noKk = noKk.equals("null") ? null : noKk;;
	}

	public String getNoKtp() {
		return noKtp;
	}

	public void setNoKtp(String noKtp) {
		this.noKtp = noKtp.equals("null") ? null : noKtp;
	}

	public String getPhoneNumber1() {
		return phoneNumber1;
	}

	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1.equals("null") ? null : phoneNumber1;
	}

	public String getPhoneNumber2() {
		return phoneNumber2;
	}

	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2.equals("null") ? null : phoneNumber2;
	}

	public String getPhoneNumber3() {
		return phoneNumber3;
	}

	public void setPhoneNumber3(String phoneNumber3) {
		this.phoneNumber3 = phoneNumber3.equals("null") ? null : phoneNumber3;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.equals("null") ? null : address;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note.equals("null") ? null : note;
	}

	public String getBpjsNo() {
		return bpjsNo;
	}

	public void setBpjsNo(String bpjsNo) {
		this.bpjsNo = bpjsNo.equals("null") ? null : bpjsNo;
	}

	public String getKisNo() {
		return kisNo;
	}

	public void setKisNo(String kisNo) {
		this.kisNo = kisNo.equals("null") ? null : kisNo;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getLastEducation() {
		return lastEducation;
	}

	public void setLastEducation(String lastEducation) {
		this.lastEducation = lastEducation;
	}
	
	
	
}