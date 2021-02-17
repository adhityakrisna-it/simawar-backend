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
 * The persistent class for the master_informasi database table.
 * 
 */
@Entity
@Table(name="master_informasi")
@NamedQuery(name="MasterInformasi.findAll", query="SELECT m FROM MasterInformasi m")
@Getter
@Setter
public class MasterInformasi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="info_id", unique=true, nullable=false, length=20)
	private String infoId;

	@Column(length=100)
	private String info1;

	@Column(length=100)
	private String info2;

	@Column(length=100)
	private String info3;

	@Column(length=100)
	private String info4;
	
}