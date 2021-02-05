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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(length=3)
	private String blok;

	@Column(length=200)
	private String cluster;

	@Column(name="family_name", nullable=false, length=100)
	private String familyName;

	@Column(length=1000)
	private String note;

	@Column(name="persil_id", length=100)
	private String persilId;

	@Column(length=2)
	private String rt;

	@Column(length=2)
	private String rw;

	@Column(name="user_id", nullable=false, length=100)
	private String userId;

	@Column(name="user_id_add", nullable=false, length=100)
	private String userIdAdd;

	@Column(name="user_id_edit", length=100)
	private String userIdEdit;



}