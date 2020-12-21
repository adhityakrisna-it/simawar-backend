package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the master_rt database table.
 * 
 */
@Embeddable
public class MasterRtPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="kelurahan_id", insertable=false, updatable=false, unique=true, nullable=false, length=100)
	private String kelurahanId;

	@Column(name="rw_id", insertable=false, updatable=false, unique=true, nullable=false)
	private int rwId;

	@Column(name="rt_id", unique=true, nullable=false)
	private int rtId;

	public MasterRtPK() {
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
	public int getRtId() {
		return this.rtId;
	}
	public void setRtId(int rtId) {
		this.rtId = rtId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MasterRtPK)) {
			return false;
		}
		MasterRtPK castOther = (MasterRtPK)other;
		return 
			this.kelurahanId.equals(castOther.kelurahanId)
			&& (this.rwId == castOther.rwId)
			&& (this.rtId == castOther.rtId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.kelurahanId.hashCode();
		hash = hash * prime + this.rwId;
		hash = hash * prime + this.rtId;
		
		return hash;
	}
}