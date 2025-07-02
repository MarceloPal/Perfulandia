package com.venta.microservice_venta;
import com.venta.microservice_venta.controller.VentaController;

import com.venta.microservice_venta.DTO.VentaDTO;
import com.venta.microservice_venta.http.response.ProductobyVentaResponse;
import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.service.VentaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductoDeVenta() {
        int idProducto = 1;
        ProductobyVentaResponse responseMock = new ProductobyVentaResponse();
        when(ventaService.obtenerProducto(idProducto)).thenReturn(responseMock);

        ResponseEntity<ProductobyVentaResponse> response = ventaController.getProductoDeVenta(idProducto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseMock, response.getBody());
    }

    @Test
    void testSaveVentaExito() {
        VentaDTO dto = new VentaDTO();
        dto.setFecha_venta(LocalDate.now());
        dto.setHora_venta(LocalTime.now());
        dto.setCosto(10.0);
        dto.setCantidad(2);
        dto.setId_producto(1);

        Venta venta = new Venta();
        venta.setId_venta(1);
        venta.setFecha_venta(dto.getFecha_venta());
        venta.setHora_venta(dto.getHora_venta());
        venta.setCosto(dto.getCosto());
        venta.setCantidad(dto.getCantidad());
        venta.setCostoTotal(dto.getCosto() * dto.getCantidad());
        venta.setId_producto(dto.getId_producto());

        when(ventaService.save(any(Venta.class))).thenReturn(venta);

        ResponseEntity<?> response = ventaController.save(dto);

        assertEquals(201, response.getStatusCode().value());
        VentaDTO responseBody = (VentaDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(dto.getId_producto(), responseBody.getId_producto());
    }

    @Test
    void testSaveVentaDataIntegrityViolation() {
        VentaDTO dto = new VentaDTO();
        when(ventaService.save(any(Venta.class))).thenThrow(DataIntegrityViolationException.class);

        ResponseEntity<?> response = ventaController.save(dto);

        assertEquals(409, response.getStatusCode().value());
        HashMap<String, String> body = (HashMap<String, String>) response.getBody();
        assertTrue(body.containsKey("error"));
    }

    @Test
    void testBuscarPorIdExistente() {
        Venta venta = new Venta();
        venta.setId_venta(1);

        when(ventaService.findById(1)).thenReturn(Optional.of(venta));

        ResponseEntity<Venta> response = ventaController.buscarPorId(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(venta, response.getBody());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        when(ventaService.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Venta> response = ventaController.buscarPorId(1);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testEliminarVentaExistente() {
        int id = 1;
        when(ventaService.existsById(id)).thenReturn(true);
        doNothing().when(ventaService).deleteById(id);

        ResponseEntity<?> response = ventaController.eliminarVenta(id);

        assertEquals(200, response.getStatusCode().value());
        HashMap<String, String> body = (HashMap<String, String>) response.getBody();
        assertTrue(body.get("message").contains("ha sido eliminada"));
    }

    @Test
    void testEliminarVentaNoExistente() {
        int id = 99;
        when(ventaService.existsById(id)).thenReturn(false);

        ResponseEntity<?> response = ventaController.eliminarVenta(id);

        assertEquals(404, response.getStatusCode().value());
        HashMap<String, String> body = (HashMap<String, String>) response.getBody();
        assertTrue(body.get("message").contains("No se encontrÃ³"));
    }

    @Test
    void testEliminarVentaException() {
        int id = 1;
        when(ventaService.existsById(id)).thenReturn(true);
        doThrow(RuntimeException.class).when(ventaService).deleteById(id);

        ResponseEntity<?> response = ventaController.eliminarVenta(id);

        assertEquals(500, response.getStatusCode().value());
        HashMap<String, String> body = (HashMap<String, String>) response.getBody();
        assertTrue(body.get("message").contains("No se pudo eliminar"));
    }
}