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
 * The persistent class for the master_role database table.
 * 
 */
@Entity
@Table(name="master_role")
@NamedQuery(name="MasterRole.findAll", query="SELECT m FROM MasterRole m")
@Getter
@Setter
public class MasterRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id", unique=true, nullable=false, length=20)
	private String roleId;

	@Column(name="role_name", nullable=false, length=100)
	private String roleName;

	public MasterRole() {
	}


}