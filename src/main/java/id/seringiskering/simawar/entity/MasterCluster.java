package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;


/**
 * The persistent class for the master_cluster database table.
 * 
 */
@Entity
@Table(name="master_cluster")
@NamedQuery(name="MasterCluster.findAll", query="SELECT m FROM MasterCluster m")
public class MasterCluster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cluster_id", unique=true, nullable=false, length=100)
	private String clusterId;

	@Column(name="cluster_name", nullable=false, length=100)
	private String clusterName;

	//bi-directional many-to-one association to MasterBlok
	@OneToMany(mappedBy="masterCluster")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<MasterBlok> masterBloks;

	//bi-directional many-to-many association to MasterRt
	@ManyToMany(mappedBy="masterClusters")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<MasterRt> masterRts;

	public MasterCluster() {
	}

	public String getClusterId() {
		return this.clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getClusterName() {
		return this.clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public Set<MasterBlok> getMasterBloks() {
		return this.masterBloks;
	}

	public void setMasterBloks(Set<MasterBlok> masterBloks) {
		this.masterBloks = masterBloks;
	}

	public MasterBlok addMasterBlok(MasterBlok masterBlok) {
		getMasterBloks().add(masterBlok);
		masterBlok.setMasterCluster(this);

		return masterBlok;
	}

	public MasterBlok removeMasterBlok(MasterBlok masterBlok) {
		getMasterBloks().remove(masterBlok);
		masterBlok.setMasterCluster(null);

		return masterBlok;
	}

	public Set<MasterRt> getMasterRts() {
		return this.masterRts;
	}

	public void setMasterRts(Set<MasterRt> masterRts) {
		this.masterRts = masterRts;
	}

}