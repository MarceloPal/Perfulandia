package com.microservice.producto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = MicroserviceProductoApplication.class)


class MicroserviceProductoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest() {
   	 	System.setProperty("spring.profiles.active", "test");
    	System.setProperty("server.port", "0"); 
    	MicroserviceProductoApplication.main(new String[] {});
	}


}

//Las pruebas unitarias se ejecutan con JUnit 5, que es la versión más reciente de JUnit.
//La anotación @SpringBootTest se utiliza para indicar que es una prueba de integración de Spring Boot.
//La anotación @ActiveProfiles("test") se utiliza para activar el perfil de configuración "test" durante la ejecución de las pruebas.
//El método contextLoads() verifica que el contexto de la aplicación se carga correctamente.
//Las pruebas unitarias aseguran que la logica interna (servicios, controladores, repositorias) funcionen correctamente en aislamiento.