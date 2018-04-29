package application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	private UserDetailsService userDetailsService;

	@Autowired
	 public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {    
		 auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
	 } 
	
	@Bean(name="passwordEncoder")
    public BCryptPasswordEncoder passwordencoder(){
     return new BCryptPasswordEncoder();
    }

	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
		 	
		 	//only for Interet Explorer to view images; disable "x-frame-option: sniff" 
			http
		 		.headers()
					.contentTypeOptions()
					.disable();
	 		
		 	http
	            .authorizeRequests()
	            	//free Resources
	                .antMatchers("/","/request","/category/**","/search","/search/**","/register","/resources/**","/webjars/**","/offer/**").permitAll()
	                //protected Resources
	                .anyRequest().authenticated()
	                .and()
	            .formLogin()
	                .loginPage("/login")
	                .permitAll()
	                .and()
	            .logout()
	            .permitAll();
		 	
		 	//for chat
		 	http
		 		.csrf().disable().sessionManagement().maximumSessions(-1).sessionRegistry(sessionRegistry());
	    }
	 
	//for chat
	 @Bean
	    public SessionRegistry sessionRegistry() {
	        return new SessionRegistryImpl();
	    }
	//for chat
	    @Bean
	    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
	        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	    }

	    
}


