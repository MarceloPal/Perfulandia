package com.venta.microservice_venta;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest
class MicroserviceVentaApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest() {
   	 	System.setProperty("spring.profiles.active", "test");
    	System.setProperty("server.port", "0"); 
    	MicroserviceVentaApplication.main(new String[] {});
	}

}
