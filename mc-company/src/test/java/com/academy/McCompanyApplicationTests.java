package com.academy;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.academy.controller.CompanyQueryController;

@SpringBootTest
class McCompanyApplicationTests {
	
	@Autowired
	private CompanyQueryController queryController;

	@Test
	void contextLoads() {
		assertNotNull(queryController, "CompanyQueryController is null");
	}

}
