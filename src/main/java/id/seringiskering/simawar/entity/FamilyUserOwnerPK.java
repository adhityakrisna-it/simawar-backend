package id.seringiskering.simawar.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the family_user_owner database table.
 * 
 */
@Embeddable
@Setter
@Getter
public class FamilyUserOwnerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	private Long id;

	@Column(name="user_id", insertable=false, updatable=false, unique=true, nullable=false, length=100)
	private String userId;


}