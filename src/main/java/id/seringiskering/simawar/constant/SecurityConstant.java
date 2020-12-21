package id.seringiskering.simawar.constant;

public class SecurityConstant {
	
	public static final long EXRIRATION_TIME = 432_000_000; //5 days expressed in miliseconds
	public static final String TOKEN_PREFIX = "Bearer ";	
	public static final String JWT_TOKEN_HEADER = "Jwt-Token";
	public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Cannot be verified";
	public static final String SERING_IS_KERING = "PT Sering Is Kering";
	public static final String SERINGISKERING_ADMINISTRATION = "SIMAWAR - Sistem Informasi Manajemen Warga";
	public static final String AUTHORITIES = "Authorities";
	public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
	public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
	public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
	public static final String[] PUBLIC_URLS = {
												"/user/login",
												"/user/register", 
												"/user/image/**",
												"/master/findAllMasterCluster",
												"/master/findMasterBlokByClusterId/**"};
	//public static final String[] PUBLIC_URLS = {"**"};
	
}
