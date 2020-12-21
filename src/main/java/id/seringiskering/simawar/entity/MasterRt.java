package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the master_rt database table.
 * 
 */
@Entity
@Table(name="master_rt")
@NamedQuery(name="MasterRt.findAll", query="SELECT m FROM MasterRt m")
public class MasterRt implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MasterRtPK id;

	//bi-directional many-to-many association to MasterBlok
	@ManyToMany
	@JoinTable(
		name="master_blok_rt"
		, joinColumns={
			@JoinColumn(name="kelurahan_id", referencedColumnName="kelurahan_id", nullable=false),
			@JoinColumn(name="rt_id", referencedColumnName="rt_id", nullable=false),
			@JoinColumn(name="rw_id", referencedColumnName="rw_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="blok_id", referencedColumnName="blok_id", nullable=false),
			@JoinColumn(name="cluster_id", referencedColumnName="cluster_id", nullable=false)
			}
		)
	private Set<MasterBlok> masterBloks;

	//bi-directional many-to-many association to MasterCluster
	@ManyToMany
	@JoinTable(
		name="master_cluster_rt"
		, joinColumns={
			@JoinColumn(name="kelurahan_id", referencedColumnName="kelurahan_id", nullable=false),
			@JoinColumn(name="rt_id", referencedColumnName="rt_id", nullable=false),
			@JoinColumn(name="rw_id", referencedColumnName="rw_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="cluster_id", nullable=false)
			}
		)
	private Set<MasterCluster> masterClusters;

	//bi-directional many-to-one association to MasterRw
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="kelurahan_id", referencedColumnName="kelurahan_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="rw_id", referencedColumnName="rw_id", nullable=false, insertable=false, updatable=false)
		})
	private MasterRw masterRw;

	public MasterRt() {
	}

	public MasterRtPK getId() {
		return this.id;
	}

	public void setId(MasterRtPK id) {
		this.id = id;
	}

	public Set<MasterBlok> getMasterBloks() {
		return this.masterBloks;
	}

	public void setMasterBloks(Set<MasterBlok> masterBloks) {
		this.masterBloks = masterBloks;
	}

	public Set<MasterCluster> getMasterClusters() {
		return this.masterClusters;
	}

	public void setMasterClusters(Set<MasterCluster> masterClusters) {
		this.masterClusters = masterClusters;
	}

	public MasterRw getMasterRw() {
		return this.masterRw;
	}

	public void setMasterRw(MasterRw masterRw) {
		this.masterRw = masterRw;
	}

}