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
 * The persistent class for the master_pekerjaan database table.
 * 
 */
@Entity
@Table(name="master_pekerjaan")
@NamedQuery(name="MasterPekerjaan.findAll", query="SELECT m FROM MasterPekerjaan m")
@Getter
@Setter
public class MasterPekerjaan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="work_id", unique=true, nullable=false, length=100)
	private String workId;

	@Column(name="work_name", nullable=false, length=100)
	private String workName;

}