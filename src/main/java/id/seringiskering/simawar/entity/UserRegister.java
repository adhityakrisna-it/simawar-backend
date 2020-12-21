package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


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
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_add", nullable=false)
	@CreationTimestamp
	private Date dateAdd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_approve")
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

	@Column(nullable=false, length=20)
	private String username;

	//bi-directional many-to-one association to MasterBlok
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="blok_id", referencedColumnName="blok_id"),
		@JoinColumn(name="cluster_id", referencedColumnName="cluster_id")
		})
	private MasterBlok masterBlok;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;


}