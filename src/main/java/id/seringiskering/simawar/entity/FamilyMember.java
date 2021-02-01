package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the family_member database table.
 * 
 */
@Entity
@Getter
@Setter
@Table(name="family_member")
@NamedQuery(name="FamilyMember.findAll", query="SELECT f FROM FamilyMember f")
public class FamilyMember implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(name="address_as_id", length=1000)
	private String addressAsId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_add", nullable=false)
	@CreationTimestamp
	private Date dateAdd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_update")
	@UpdateTimestamp
	private Date dateUpdate;

	@Column(name="family_status", length=100)
	private String familyStatus;

	@Column(name="kependudukan_status", length=20)
	private String kependudukanStatus;

	@Column(name="kk_url", length=1000)
	private String kkUrl;

	@Column(name="ktp_url", length=1000)
	private String ktpUrl;

	@Column(nullable=false, length=100)
	private String name;

	@Column(name="no_kk", length=20)
	private String noKk;

	@Column(name="no_ktp", length=16)
	private String noKtp;

	@Column(name="phone_number1", nullable=false, length=100)
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

	@Column(length=100)
	private String work;
	
	@Column(length=1000)
	private String note;	
	
	@Column(name="bpjs_no", length=20)
	private String bpjsNo;
	
	@Column(name="kis_no", length=20)
	private String kisNo;
	
	@Column(name="blood_type", length=10)
	private String bloodType;

	@Column(name="last_education", length=100)
	private String lastEducation;
	
	@Temporal(TemporalType.DATE)
	@Column(name="birth_date")
	private Date birthDate;	

	//bi-directional many-to-one association to Family
	@ManyToOne
	@JoinColumn(name="family_id")
	private Family family;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id_edit")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id_add", nullable=false)
	private User user2;
	
	@Column(length=100)
	private String address;	

}