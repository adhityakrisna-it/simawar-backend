package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


/**
 * The persistent class for the family database table.
 * 
 */
@Entity
@Getter
@Setter
@Table(name="family")
@NamedQuery(name="Family.findAll", query="SELECT f FROM Family f")
public class Family implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private String id;

	@Column(name="family_name", nullable=false, length=100)
	private String familyName;

	@Column(length=1000)
	private String note;

	@Column(name="persil_id", length=100)
	private String persilId;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id_add", nullable=false)
	private User user2;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id_edit")
	private User user3;

	//bi-directional many-to-one association to FamilyMember
	@OneToMany(mappedBy="family")
	private Set<FamilyMember> familyMembers;


}