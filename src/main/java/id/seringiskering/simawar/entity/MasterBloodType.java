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
 * The persistent class for the master_blood_type database table.
 * 
 */
@Entity
@Table(name="master_blood_type")
@NamedQuery(name="MasterBloodType.findAll", query="SELECT m FROM MasterBloodType m")
@Getter
@Setter
public class MasterBloodType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="blood_type_id", unique=true, nullable=false, length=5)
	private String bloodTypeId;

}