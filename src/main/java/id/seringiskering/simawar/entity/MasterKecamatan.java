package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the master_kecamatan database table.
 * 
 */
@Entity
@Table(name="master_kecamatan")
@NamedQuery(name="MasterKecamatan.findAll", query="SELECT m FROM MasterKecamatan m")
public class MasterKecamatan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="kecamatan_id", unique=true, nullable=false, length=20)
	private String kecamatanId;

	@Column(name="kecamatan_name", nullable=false, length=100)
	private String kecamatanName;

	//bi-directional many-to-one association to MasterKelurahan
	@OneToMany(mappedBy="masterKecamatan")
	private Set<MasterKelurahan> masterKelurahans;

	public MasterKecamatan() {
	}

	public String getKecamatanId() {
		return this.kecamatanId;
	}

	public void setKecamatanId(String kecamatanId) {
		this.kecamatanId = kecamatanId;
	}

	public String getKecamatanName() {
		return this.kecamatanName;
	}

	public void setKecamatanName(String kecamatanName) {
		this.kecamatanName = kecamatanName;
	}

	public Set<MasterKelurahan> getMasterKelurahans() {
		return this.masterKelurahans;
	}

	public void setMasterKelurahans(Set<MasterKelurahan> masterKelurahans) {
		this.masterKelurahans = masterKelurahans;
	}

	public MasterKelurahan addMasterKelurahan(MasterKelurahan masterKelurahan) {
		getMasterKelurahans().add(masterKelurahan);
		masterKelurahan.setMasterKecamatan(this);

		return masterKelurahan;
	}

	public MasterKelurahan removeMasterKelurahan(MasterKelurahan masterKelurahan) {
		getMasterKelurahans().remove(masterKelurahan);
		masterKelurahan.setMasterKecamatan(null);

		return masterKelurahan;
	}

}