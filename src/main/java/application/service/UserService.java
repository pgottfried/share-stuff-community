package application.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.repository.UserRepository;


@Service
public class UserService implements UserDetailsService{
	
	@Autowired 
	private UserRepository userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		application.entity.User foundUser = null;
		foundUser = userRepo.findByUserName(userName);
		if(foundUser == null){
			throw new UsernameNotFoundException("No user present with username: "+userName);
		}
		else{
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			
			User userDetails = new User(foundUser.getUserName(), foundUser.getPassword(), authorities);
			return userDetails;
		}
	}
	
	@Transactional(readOnly=true)
	public application.entity.User findByUserName(String userName){
		application.entity.User user = userRepo.findByUserName(userName);
		return user;
	}
	
	
}
