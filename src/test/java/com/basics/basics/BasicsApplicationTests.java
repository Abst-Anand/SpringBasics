package com.basics.basics;

import com.basics.basics.entities.User;
import com.basics.basics.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BasicsApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
	}

	@Test
	void testJwtService() {
		User user = new User(4L, "anand@gmail.com", "12345","simp");

		String token = jwtService.generateAccessToken(user);

		System.out.println(token);

		// tempered token
//		Long userId = jwtService.getUserIdFromToken("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiI0IiwiZW1haWwiOiJhbmFuZEBnbWFpbC5jb20iLCJyb2xlcyI6WyJVU0VSIiwiQURNSU4iXSwiaWF0IjoxNzI5Nzc1MzA0LCJleHAiOjE3Mjk3NzUzNjR9.kjbsdfjbsiknakfkaek");
		Long userId = jwtService.getUserIdFromToken(token);

		System.out.println(userId);

	}

}
