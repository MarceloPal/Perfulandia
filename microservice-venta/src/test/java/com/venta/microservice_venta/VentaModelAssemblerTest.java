package com.venta.microservice_venta;

import com.venta.microservice_venta.assemblers.VentaModelAssembler;
import com.venta.microservice_venta.model.Venta;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.assertj.core.api.Assertions.assertThat;

public class VentaModelAssemblerTest {

    private final VentaModelAssembler assembler = new VentaModelAssembler();

    @Test
    void toModel_ShouldCreateEntityModelWithLinks() {
        // Arrange
        Venta venta = new Venta();
        venta.setId_venta(1);

        // Act
        EntityModel<Venta> entityModel = assembler.toModel(venta);

        // Assert
        assertThat(entityModel.getContent()).isEqualTo(venta);

        // Verificar que el link self exista y sea correcto
        Link selfLink = entityModel.getLink("self").orElseThrow();
        assertThat(selfLink.getHref()).contains("/1");

        // Verificar que el link "ventas" exista
        Link ventasLink = entityModel.getLink("ventas").orElseThrow();
        assertThat(ventasLink.getHref()).contains("/ventas");
    }
}
