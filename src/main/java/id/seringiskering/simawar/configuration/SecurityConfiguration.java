package id.seringiskering.simawar.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import id.seringiskering.simawar.constant.SecurityConstant;
import id.seringiskering.simawar.filter.JwtAccessDeniedHandler;
import id.seringiskering.simawar.filter.JwtAuthenticationEntryPoint;
import id.seringiskering.simawar.filter.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private JwtAuthorizationFilter jwtAuthorization;
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private UserDetailsService userDetailsService; 
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public SecurityConfiguration(JwtAuthorizationFilter jwtAuthorization, 
			JwtAccessDeniedHandler jwtAccessDeniedHandler,
			JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, 
			@Qualifier("userDetailsService") UserDetailsService userDetailsService, // untuk memberitahu spring untuk menggunakan UserServiceImpl
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.jwtAuthorization = jwtAuthorization;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("SECURITY CONSTANT " + SecurityConstant.PUBLIC_URLS.toString());
		
		
		http.csrf().disable().cors().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().authorizeRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and().addFilterBefore(jwtAuthorization, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	
}
