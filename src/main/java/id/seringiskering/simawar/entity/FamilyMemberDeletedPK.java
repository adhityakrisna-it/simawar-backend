package id.seringiskering.simawar.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the family_member_deleted database table.
 * 
 */
@Embeddable
@Getter
@Setter
public class FamilyMemberDeletedPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_log", unique=true, nullable=false)
	@CreatedDate
	private java.util.Date dateLog;

}