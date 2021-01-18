package id.seringiskering.simawar.constant;

public class Authority {

	public static final String[] USER_AUTHORITIES = {"user:read"}; // BASE ON YOUR APPLICATION
	public static final String[] HR_AUTHORITIES = {"user:read", "user:update", "warga:read"};
	public static final String[] MANAGER_AUTHORITIES = {"user:read", "user:create", "warga:read"};
	public static final String[] ADMIN_AUTHORITIES = {"user:read", "user:create", "user:update", "user:approval", "warga:read"};
	public static final String[] SUPER_ADMIN_AUTHORITIES = {"user:read", "user:create", "user:update","user:delete", "user:approval", "warga:read"};
	public static final String[] PENGURUS_RW_AUTHORITIES = {"user:read", "user:create", "user:update", "user:approval", "warga:read"};
	public static final String[] PENGURUS_RT_AUTHORITIES = {"user:read", "user:create", "user:update", "user:approval", "warga:read"};
	public static final String[] WARGA_AUTHORITIES = {"user:read" , "warga:read"};
}
