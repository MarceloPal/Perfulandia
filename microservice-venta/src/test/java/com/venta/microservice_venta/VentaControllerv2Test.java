package com.venta.microservice_venta;


import com.venta.microservice_venta.DTO.VentaDTO;
import com.venta.microservice_venta.assemblers.VentaModelAssembler;
import com.venta.microservice_venta.controller.VentaControllerv2;
import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VentaControllerv2Test {

    @InjectMocks
    private VentaControllerv2 controller;

    @Mock
    private VentaService ventaService;

    @Mock
    private VentaModelAssembler assembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void all_returnsCollectionModel() {
        Venta venta = new Venta();
        venta.setId_venta(1);
        EntityModel<Venta> entityModel = EntityModel.of(venta);
        List<EntityModel<Venta>> list = List.of(entityModel);
        CollectionModel<EntityModel<Venta>> collectionModel = CollectionModel.of(list);

        when(ventaService.findAll()).thenReturn(List.of(venta));
        when(assembler.toModel(venta)).thenReturn(entityModel);

        CollectionModel<EntityModel<Venta>> result = controller.all();

        assertThat(result.getContent()).contains(entityModel);
    }

    @Test
    void getVentaById_found() {
        Venta venta = new Venta();
        venta.setId_venta(1);
        EntityModel<Venta> entityModel = EntityModel.of(venta);

        when(ventaService.findById(1)).thenReturn(Optional.of(venta));
        when(assembler.toModel(venta)).thenReturn(entityModel);

        ResponseEntity<?> response = controller.getVentaById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(entityModel);
    }

    @Test
    void getVentaById_notFound() {
        when(ventaService.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getVentaById(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> error = (Map<?, ?>) response.getBody();
        assertThat(error.get("message")).isEqualTo("Venta no encontrada");
    }

    @Test
    void save_createsVenta() {
        VentaDTO dto = new VentaDTO();
        dto.setFecha_venta(LocalDate.now());
        dto.setHora_venta(LocalTime.now());
        dto.setCosto(100.0);
        dto.setCantidad(2);
        dto.setId_producto(5);

        Venta venta = new Venta();
        venta.setId_venta(1);
        venta.setFecha_venta(dto.getFecha_venta());
        venta.setHora_venta(dto.getHora_venta());
        venta.setCosto(dto.getCosto());
        venta.setCantidad(dto.getCantidad());
        venta.setId_producto(dto.getId_producto());

        Venta saved = new Venta();
        saved.setId_venta(1);
        EntityModel<Venta> entityModel = EntityModel.of(saved);

        when(ventaService.save(any(Venta.class))).thenReturn(saved);
        when(assembler.toModel(saved)).thenReturn(entityModel);

        ResponseEntity<?> response = controller.save(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(entityModel);
    }

    @Test
    void delete_found() {
        when(ventaService.existsById(1)).thenReturn(true);
        doNothing().when(ventaService).deleteById(1);

        ResponseEntity<?> response = controller.delete(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Venta eliminada");
    }

    @Test
    void delete_notFound() {
        when(ventaService.existsById(99)).thenReturn(false);

        ResponseEntity<?> response = controller.delete(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Venta no encontrada");
    }
}
