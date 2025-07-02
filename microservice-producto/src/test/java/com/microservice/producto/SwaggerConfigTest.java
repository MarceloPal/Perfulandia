package com.microservice.producto;
import com.microservice.producto.config.SwaggerConfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    private final SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    void customOpenAPI_ShouldReturnConfiguredOpenAPI() {
        // Act
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        // Assert
        assertThat(openAPI).isNotNull();
        Info info = openAPI.getInfo();
        assertThat(info).isNotNull();
        assertThat(info.getTitle()).isEqualTo("API Producto - Perfulandia");
        assertThat(info.getVersion()).isEqualTo("1.0.0");
        assertThat(info.getDescription()).isEqualTo("Documentación del microservicio de productos.");
    }
}
