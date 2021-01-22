package id.seringiskering.simawar.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

	/**
	 * 
	 * 
	 * 
	 */

	@Id
	@Column(nullable = false, updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userId;

	private String firstName;
	private String lastName;
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private String email;
	private String profileImageUrl;
	private Date lastLoginDate;
	private Date lastLoginDateDisplay;

	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date joinDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date editingDate;

	private String role; // ROLE_USER{ read, edit }, ROLE_ADMIN {delete}
	private String[] authorities;
	private boolean isActive;
	private boolean isNotLocked;
	private String userDataProfile;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private UserPersil userPersil;

}
