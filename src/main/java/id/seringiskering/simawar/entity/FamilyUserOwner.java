package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the family_user_owner database table.
 * 
 */
@Entity
@Table(name="family_user_owner")
@NamedQuery(name="FamilyUserOwner.findAll", query="SELECT f FROM FamilyUserOwner f")
@Setter
@Getter
public class FamilyUserOwner implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FamilyUserOwnerPK id;

	@Column(name="date_add", nullable=false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)	
	private Date dateAdd;

	//bi-directional many-to-one association to Family
	@ManyToOne
	@JoinColumn(name="id", nullable=false, insertable=false, updatable=false)
	private Family family;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
	private User user;
	
}