package com.microservice.config;

import org.springframework.boot.SpringApplication; // permite iniciar una aplicación Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; //importa la anotación para marcar la clase como una aplicación Spring Boot
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer //convierte esta aplicación en un servidor de configuración centralizado
// permite que otros microservicios obtengan su configuración desde este servidor
@SpringBootApplication //marca la clase como la clase principal de arrranque de una aplicación Spring Boot
public class MicroserviceConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConfigApplication.class, args);
	}

}
