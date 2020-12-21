package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the master_rw database table.
 * 
 */
@Entity
@Table(name="master_rw")
@NamedQuery(name="MasterRw.findAll", query="SELECT m FROM MasterRw m")
public class MasterRw implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MasterRwPK id;

	@Column(name="city_name", nullable=false, length=100)
	private String cityName;

	@Column(name="kecamatan_name", nullable=false, length=100)
	private String kecamatanName;

	@Column(name="kelurahan_name", nullable=false, length=100)
	private String kelurahanName;

	@Column(name="province_name", nullable=false, length=100)
	private String provinceName;

	//bi-directional many-to-one association to MasterRt
	@OneToMany(mappedBy="masterRw")
	private Set<MasterRt> masterRts;

	//bi-directional many-to-one association to MasterKelurahan
	@ManyToOne
	@JoinColumn(name="kelurahan_id", nullable=false, insertable=false, updatable=false)
	private MasterKelurahan masterKelurahan;

	public MasterRw() {
	}

	public MasterRwPK getId() {
		return this.id;
	}

	public void setId(MasterRwPK id) {
		this.id = id;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getKecamatanName() {
		return this.kecamatanName;
	}

	public void setKecamatanName(String kecamatanName) {
		this.kecamatanName = kecamatanName;
	}

	public String getKelurahanName() {
		return this.kelurahanName;
	}

	public void setKelurahanName(String kelurahanName) {
		this.kelurahanName = kelurahanName;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Set<MasterRt> getMasterRts() {
		return this.masterRts;
	}

	public void setMasterRts(Set<MasterRt> masterRts) {
		this.masterRts = masterRts;
	}

	public MasterRt addMasterRt(MasterRt masterRt) {
		getMasterRts().add(masterRt);
		masterRt.setMasterRw(this);

		return masterRt;
	}

	public MasterRt removeMasterRt(MasterRt masterRt) {
		getMasterRts().remove(masterRt);
		masterRt.setMasterRw(null);

		return masterRt;
	}

	public MasterKelurahan getMasterKelurahan() {
		return this.masterKelurahan;
	}

	public void setMasterKelurahan(MasterKelurahan masterKelurahan) {
		this.masterKelurahan = masterKelurahan;
	}

}