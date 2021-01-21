package id.seringiskering.simawar.response.user;

import java.util.Date;

import id.seringiskering.simawar.profile.UserProfile;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
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
    private UserProfile userDataProfile;
}
