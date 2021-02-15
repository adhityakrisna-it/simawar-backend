package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the family_member_deleted database table.
 * 
 */
@Entity
@Table(name="family_member_deleted")
@NamedQuery(name="FamilyMemberDeleted.findAll", query="SELECT f FROM FamilyMemberDeleted f")
@Getter
@Setter
public class FamilyMemberDeleted implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FamilyMemberDeletedPK id;

	@Column(length=100)
	private String address;

	@Column(name="address_as_id", length=1000)
	private String addressAsId;

	@Temporal(TemporalType.DATE)
	@Column(name="birth_date")
	private Date birthDate;

	@Column(name="blood_type", length=10)
	private String bloodType;

	@Column(name="bpjs_no", length=20)
	private String bpjsNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_add", nullable=false)
	private Date dateAdd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_update")
	private Date dateUpdate;

	@Column(name="family_status", length=100)
	private String familyStatus;

	@Column(name="kependudukan_status", length=20)
	private String kependudukanStatus;

	@Column(name="kis_no", length=20)
	private String kisNo;

	@Column(name="kk_url", length=1000)
	private String kkUrl;

	@Column(name="ktp_url", length=1000)
	private String ktpUrl;

	@Column(name="last_education", length=100)
	private String lastEducation;

	@Column(nullable=false, length=100)
	private String name;

	@Column(name="no_kk", length=20)
	private String noKk;

	@Column(name="no_ktp", length=20)
	private String noKtp;

	@Column(length=1000)
	private String note;

	@Column(name="phone_number1", length=100)
	private String phoneNumber1;

	@Column(name="phone_number2", length=100)
	private String phoneNumber2;

	@Column(name="phone_number3", length=100)
	private String phoneNumber3;

	@Column(name="profile_url", length=1000)
	private String profileUrl;

	@Column(length=20)
	private String religion;

	@Column(length=1)
	private String sex;

	@Column(name="user_id_add", length=100)
	private String userIdAdd;

	@Column(name="user_id_edit", length=100)
	private String userIdEdit;

	@Column(length=100)
	private String work;

}