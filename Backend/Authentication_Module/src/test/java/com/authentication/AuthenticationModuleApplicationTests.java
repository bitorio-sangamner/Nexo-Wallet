package com.authentication;

import com.authentication.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthenticationModuleApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Test
	void contextLoads() {
	}

	@Test
	public void repoTest()
	{
		String className=this.userRepository.getClass().getName();
		String packageName=this.userRepository.getClass().getPackageName();
		System.out.println("implementation classname(provided by spring) of UserRepository :"+className);
		System.out.println("package :"+packageName);

	}

}
