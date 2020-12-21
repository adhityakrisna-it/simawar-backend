package id.seringiskering.simawar.constant;

public class Authority {

	public static final String[] USER_AUTHORITIES = {"user:read"}; // BASE ON YOUR APPLICATION
	public static final String[] HR_AUTHORITIES = {"user:read", "user:update"};
	public static final String[] MANAGER_AUTHORITIES = {"user:read", "user:create"};
	public static final String[] ADMIN_AUTHORITIES = {"user:read", "user:create", "user:update", "user:approval"};
	public static final String[] SUPER_ADMIN_AUTHORITIES = {"user:read", "user:create", "user:update","user:delete"};
	public static final String[] PENGURUS_RW_AUTHORITIES = {"user:read", "user:create", "user:update", "user:approval"};
	public static final String[] PENGURUS_RT_AUTHORITIES = {"user:read", "user:create", "user:update", "user:approval"};
	
}
