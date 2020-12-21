package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the master_blok database table.
 * 
 */
@Embeddable
public class MasterBlokPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="cluster_id", insertable=false, updatable=false, unique=true, nullable=false, length=100)
	private String clusterId;

	@Column(name="blok_id", unique=true, nullable=false, length=20)
	private String blokId;

	public MasterBlokPK() {
	}
	public String getClusterId() {
		return this.clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getBlokId() {
		return this.blokId;
	}
	public void setBlokId(String blokId) {
		this.blokId = blokId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MasterBlokPK)) {
			return false;
		}
		MasterBlokPK castOther = (MasterBlokPK)other;
		return 
			this.clusterId.equals(castOther.clusterId)
			&& this.blokId.equals(castOther.blokId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.clusterId.hashCode();
		hash = hash * prime + this.blokId.hashCode();
		
		return hash;
	}
}