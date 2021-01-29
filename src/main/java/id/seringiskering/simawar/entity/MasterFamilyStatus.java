package id.seringiskering.simawar.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * The persistent class for the master_family_status database table.
 * 
 */
@Entity
@Table(name="master_family_status")
@NamedQuery(name="MasterFamilyStatus.findAll", query="SELECT m FROM MasterFamilyStatus m")
@Getter
@Setter
public class MasterFamilyStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="status_fam_id", unique=true, nullable=false, length=100)
	private String statusFamId;

	@Column(name="status_fam_name", nullable=false, length=100)
	private String statusFamName;

}