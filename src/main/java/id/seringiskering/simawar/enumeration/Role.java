package id.seringiskering.simawar.enumeration;

import static id.seringiskering.simawar.constant.Authority.USER_AUTHORITIES;
import static id.seringiskering.simawar.constant.Authority.ADMIN_AUTHORITIES;
import static id.seringiskering.simawar.constant.Authority.HR_AUTHORITIES;
import static id.seringiskering.simawar.constant.Authority.MANAGER_AUTHORITIES;
import static id.seringiskering.simawar.constant.Authority.SUPER_ADMIN_AUTHORITIES;


public enum Role {
	ROLE_USER(USER_AUTHORITIES),
	ROLE_HR(HR_AUTHORITIES),
	ROLE_MANAGER(MANAGER_AUTHORITIES),
	ROLE_ADMIN(ADMIN_AUTHORITIES),
	ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);
	
	private String[] authorities;
	
	Role(String... authorities) {
		this.authorities = authorities;
	}
	
	public String[] getAuthorities() {
		return authorities;
	}
	
}
