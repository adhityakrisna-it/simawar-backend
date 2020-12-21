package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


/**
 * The persistent class for the master_blok database table.
 * 
 */
@Entity
@Table(name="master_blok")
@NamedQuery(name="MasterBlok.findAll", query="SELECT m FROM MasterBlok m")
@Getter
@Setter
public class MasterBlok implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MasterBlokPK id;

	@Column(name="blok_name", nullable=false, length=100)
	private String blokName;

	//bi-directional many-to-one association to MasterCluster
	@ManyToOne
	@JoinColumn(name="cluster_id", nullable=false, insertable=false, updatable=false)
	private MasterCluster masterCluster;
//
//	//bi-directional many-to-one association to UserRegister
//	@OneToMany(mappedBy="masterBlok")
//	private Set<UserRegister> userRegisters;
//

}