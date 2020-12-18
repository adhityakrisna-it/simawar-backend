package id.seringiskering.simawar.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user")
public class User {

	/**
	 * 
	 * 
	 * user_id varchar(100) PK 
	username varchar(20) 
first_name varchar(100) 
last_name varchar(100) 
password varchar(100) 
email varchar(100) 
profile_image_url varchar(1000) 
last_login_date datetime 
last_login_date_display datetime 
join_date datetime 
editing_date datetime 
role varchar(100) 
authorities tinyblob 
is_active int(11) 
is_not_locked int(11)
	 */
	
	@Id
	@Column(nullable = false, updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;
	
    private String firstName;
    private String lastName;
    private String username;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private Date editingDate;
    private String role; //ROLE_USER{ read, edit }, ROLE_ADMIN {delete}
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;
	
}
