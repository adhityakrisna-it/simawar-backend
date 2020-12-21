package id.seringiskering.simawar.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the user_register database table.
 * 
 */
@Entity
@Table(name="user_register")
@NamedQuery(name="UserRegister.findAll", query="SELECT u FROM UserRegister u")
@Getter
@Setter
public class UserRegister implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="blok_id", length=20)
	private String blokId;

	@Column(name="blok_identity", length=2)
	private String blokIdentity;

	@Column(name="blok_number")
	private int blokNumber;

	@Column(name="cluster_id", length=100)
	private String clusterId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_add", nullable=false)
	@CreationTimestamp
	private Date dateAdd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_approve", nullable = true)
	@UpdateTimestamp
	private Date dateApprove;

	@Column(nullable=false, length=100)
	private String email;

	@Column(name="first_name", nullable=false, length=100)
	private String firstName;

	@Column(name="last_name", nullable=false, length=100)
	private String lastName;

	@Column(nullable=false, length=100)
	private String password;

	@Column(name="user_id", length=100)
	private String userId;

	@Column(nullable=false, length=20)
	private String username;
	
	@Column(columnDefinition = "varchar(20) not null default 'entri'")
	private String registerStatus = "entri";
	
	@Column(name="kelurahan_id", length=100)
	private String kelurahanId;

	@Column(name="rt_id")
	private Integer rtId;

	@Column(name="rw_id")
	private Integer rwId;	
	
	@Column(name="persil_id", length=100)
	private String persilId;
	
}