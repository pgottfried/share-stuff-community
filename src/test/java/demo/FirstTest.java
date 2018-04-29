package demo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import application.service.UserService;

public class FirstTest {
	
	@Autowired UserService userService;
	
	@Test
	public void testFindByUserName(String userName){
		
		userService.findByUserName("Paul");
		
	}
}
