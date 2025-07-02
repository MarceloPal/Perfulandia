package com.microservice.producto;

import com.microservice.producto.assemblers.ProductoModelAssembler;
import com.microservice.producto.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductoModelAssemblerTest {

    private final ProductoModelAssembler assembler = new ProductoModelAssembler();

    @Test
    void toModel_ShouldCreateEntityModelWithLinks() {
        // Arrange
        Producto producto = new Producto();
        producto.setId_producto(1); // Usa int o Integer según tu modelo
        producto.setNombre("Producto Test");
        producto.setPrecio(100.0);

        // Act
        EntityModel<Producto> entityModel = assembler.toModel(producto);

        // Assert
        assertThat(entityModel.getContent()).isEqualTo(producto);

        // Verificar que el link self exista y sea correcto
        Link selfLink = entityModel.getLink("self").orElseThrow();
        assertThat(selfLink.getHref()).contains("/1");

        // Verificar que el link "productos" exista
        Link productosLink = entityModel.getLink("productos").orElseThrow();
        assertThat(productosLink.getHref()).contains("/productos");
    }
}
