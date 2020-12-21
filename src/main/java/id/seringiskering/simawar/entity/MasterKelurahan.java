package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the master_kelurahan database table.
 * 
 */
@Entity
@Table(name="master_kelurahan")
@NamedQuery(name="MasterKelurahan.findAll", query="SELECT m FROM MasterKelurahan m")
public class MasterKelurahan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="kelurahan_id", unique=true, nullable=false, length=100)
	private String kelurahanId;

	@Column(name="kelurahan_name", nullable=false, length=100)
	private String kelurahanName;

	//bi-directional many-to-one association to MasterKecamatan
	@ManyToOne
	@JoinColumn(name="kecamatan_id", nullable=false)
	private MasterKecamatan masterKecamatan;

	//bi-directional many-to-one association to MasterRw
	@OneToMany(mappedBy="masterKelurahan")
	private Set<MasterRw> masterRws;

	public MasterKelurahan() {
	}

	public String getKelurahanId() {
		return this.kelurahanId;
	}

	public void setKelurahanId(String kelurahanId) {
		this.kelurahanId = kelurahanId;
	}

	public String getKelurahanName() {
		return this.kelurahanName;
	}

	public void setKelurahanName(String kelurahanName) {
		this.kelurahanName = kelurahanName;
	}

	public MasterKecamatan getMasterKecamatan() {
		return this.masterKecamatan;
	}

	public void setMasterKecamatan(MasterKecamatan masterKecamatan) {
		this.masterKecamatan = masterKecamatan;
	}

	public Set<MasterRw> getMasterRws() {
		return this.masterRws;
	}

	public void setMasterRws(Set<MasterRw> masterRws) {
		this.masterRws = masterRws;
	}

	public MasterRw addMasterRw(MasterRw masterRw) {
		getMasterRws().add(masterRw);
		masterRw.setMasterKelurahan(this);

		return masterRw;
	}

	public MasterRw removeMasterRw(MasterRw masterRw) {
		getMasterRws().remove(masterRw);
		masterRw.setMasterKelurahan(null);

		return masterRw;
	}

}