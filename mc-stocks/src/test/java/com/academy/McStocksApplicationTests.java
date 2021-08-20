package com.academy;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.academy.controller.StockQueryController;

@SpringBootTest
class McStocksApplicationTests {
	
	@Autowired
	private StockQueryController queryController;

	@Test
	void contextLoads() {
		assertNotNull(queryController, "StockQueryController is null");
	}

}
