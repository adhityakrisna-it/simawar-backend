package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the persil database table.
 * 
 */
@Entity
@Table(name="persil")
@NamedQuery(name="Persil.findAll", query="SELECT p FROM Persil p")
@Getter
@Setter
public class Persil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="persil_id", unique=true, nullable=false, length=100)
	private String persilId;

	@Column(name="blok_id", nullable=false, length=20)
	private String blokId;

	@Column(name="blok_identity", nullable=true, length=2)
	private String blokIdentity;

	@Column(name="blok_name", nullable=false, length=100)
	private String blokName;

	@Column(name="blok_number", nullable=false)
	private int blokNumber;

	@Column(name="cluster_id", nullable=false, length=100)
	private String clusterId;

	@Column(name="family_id")
	private java.math.BigInteger familyId;

	@Column(name="kelurahan_id", length=100)
	private String kelurahanId;

	@Column(length=1000)
	private String note;

	@Column(name="rt_id")
	private Integer rtId;

	@Column(name="rw_id")
	private Integer rwId;

	public Persil() {
	}

}