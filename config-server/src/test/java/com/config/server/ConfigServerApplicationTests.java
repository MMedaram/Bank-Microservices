package com.config.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ConfigServerApplicationTests {


	@Autowired
	private ConfigServerApplication configServerApplication;

	@Test
	public void contextLoads() {
	}
}
