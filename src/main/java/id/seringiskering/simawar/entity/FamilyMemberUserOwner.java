package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the family_member_user_owner database table.
 * 
 */
@Entity
@Table(name="family_member_user_owner")
@NamedQuery(name="FamilyMemberUserOwner.findAll", query="SELECT f FROM FamilyMemberUserOwner f")
@Getter
@Setter
public class FamilyMemberUserOwner implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FamilyMemberUserOwnerPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_add", nullable=false)
	@CreationTimestamp
	private Date dateAdd;

	//bi-directional many-to-one association to FamilyMember
	@ManyToOne
	@JoinColumn(name="id", nullable=false, insertable=false, updatable=false)
	private FamilyMember familyMember;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
	private User user;


}