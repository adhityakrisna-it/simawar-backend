package id.seringiskering.simawar.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the user_persil database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "user_persil")
@NamedQuery(name = "UserPersil.findAll", query = "SELECT u FROM UserPersil u")
public class UserPersil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length = 3)
	private String blok;

	@Column(length = 200)
	private String kecamatan;

	@Column(length = 200)
	private String kelurahan;

	@Column(length = 200)
	private String kota;

	@Column(name = "nama_jalan", length = 200)
	private String namaJalan;

	@Column(length = 3)
	private String nomor;

	@Column(name = "nomor_tambahan", length = 2)
	private String nomorTambahan;

	@Column(length = 200)
	private String provinsi;

	@Column(length = 2)
	private String rt;

	@Column(length = 2)
	private String rw;
	
	@Column(length=200)
	private String cluster;
	
	@Column(length=200)
	private String perumahan;

	// bi-directional many-to-one association to User
	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private User user;

	@Id
	@Column(name="user_id")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String userId;

}