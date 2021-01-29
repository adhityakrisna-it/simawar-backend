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
 * The persistent class for the master_last_education database table.
 * 
 */
@Entity
@Table(name="master_last_education")
@NamedQuery(name="MasterLastEducation.findAll", query="SELECT m FROM MasterLastEducation m")
@Getter
@Setter
public class MasterLastEducation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="last_edu_id", unique=true, nullable=false, length=100)
	private String lastEduId;

	@Column(name="last_edu_name", nullable=false, length=100)
	private String lastEduName;

}