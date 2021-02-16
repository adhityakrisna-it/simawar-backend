package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the family_deleted database table.
 * 
 */
@Entity
@Table(name="family_deleted")
@NamedQuery(name="FamilyDeleted.findAll", query="SELECT f FROM FamilyDeleted f")
@Setter
@Getter
public class FamilyDeleted implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FamilyDeletedPK id;

	@Column(length=3)
	private String blok;

	@Column(length=200)
	private String cluster;

	@Column(name="family_name", nullable=false, length=100)
	private String familyName;

	@Column(name="head_of_family", length=100)
	private String headOfFamily;

	@Column(name="kepemilikan_status", length=20)
	private String kepemilikanStatus;

	@Column(name="kk_url", length=1000)
	private String kkUrl;

	@Column(length=3)
	private String nomor;

	@Column(length=1000)
	private String note;

	@Column(name="persil_id", length=100)
	private String persilId;

	@Column(name="profile_url", length=1000)
	private String profileUrl;

	@Column(length=2)
	private String rt;

	@Column(length=2)
	private String rw;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

}