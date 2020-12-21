package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the master_rw database table.
 * 
 */
@Embeddable
public class MasterRwPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="kelurahan_id", insertable=false, updatable=false, unique=true, nullable=false, length=100)
	private String kelurahanId;

	@Column(name="rw_id", unique=true, nullable=false)
	private int rwId;

	public MasterRwPK() {
	}
	public String getKelurahanId() {
		return this.kelurahanId;
	}
	public void setKelurahanId(String kelurahanId) {
		this.kelurahanId = kelurahanId;
	}
	public int getRwId() {
		return this.rwId;
	}
	public void setRwId(int rwId) {
		this.rwId = rwId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MasterRwPK)) {
			return false;
		}
		MasterRwPK castOther = (MasterRwPK)other;
		return 
			this.kelurahanId.equals(castOther.kelurahanId)
			&& (this.rwId == castOther.rwId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.kelurahanId.hashCode();
		hash = hash * prime + this.rwId;
		
		return hash;
	}
}