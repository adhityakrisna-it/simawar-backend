package id.seringiskering.simawar.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {
	
	private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5; // boleh diujicoba gagal password sampai dengan 5 kali, pada uji coba ke - 6 akan menyebabkan kegagalan
	private static final int ATTEMPT_INCREMENT = 1;
	private LoadingCache<String, Integer> loginAttemptCache;
	
	public LoginAttemptService() {
		super();
		loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
					.maximumSize(100)
					.build(new CacheLoader<String, Integer>() {
						public Integer load(String key) {
							return 0;
						}
					});
	}
	
	public void evictUserFromLoginAttemptCache(String username) {
		loginAttemptCache.invalidate(username);
	}
	
	public void addUserToLoginAttemptCache(String username) {
		int attempts = 0;
		try {
			attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loginAttemptCache.put(username, attempts);		
	}
	
	public boolean hasExceedMaxAttempts(String username) {
		try {
			return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS ;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
