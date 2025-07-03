package com.microservice.producto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //indica que esta clase es una configuración de Spring
public class SwaggerConfig {
    //Un bean es un obejto de spring, basicamente cuando tu indicas que algo es un bean,
    //le dices a spring  que cree este objeto y lo guarde en el contenedor de Spring,
    //para que pueda ser inyectado en otras partes de la aplicación.
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Producto - Perfulandia")
                .version("1.0.0")
                .description("Documentación del microservicio de productos."));
    }
}






// Este archivo configura Swagger para documentar la API del microservicio de productos.    
// Define un bean OpenAPI que contiene información básica como el título, la versión y la descripción de la API.