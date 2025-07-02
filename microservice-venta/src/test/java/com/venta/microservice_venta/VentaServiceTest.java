package com.venta.microservice_venta;

import com.venta.microservice_venta.client.ProductoClient;
import com.venta.microservice_venta.http.response.ProductobyVentaResponse;
import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.repository.VentaRepository;
import com.venta.microservice_venta.service.VentaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @InjectMocks
    private VentaService ventaService;

    @Mock
    private ProductoClient productoClient;

    @Mock
    private VentaRepository ventaRepository;

    @Test
    void testSaveVenta() {
        Venta venta = new Venta();
        venta.setId_venta(1);
        venta.setFecha_venta(LocalDate.now());
        venta.setHora_venta(LocalTime.now());
        venta.setCosto(50.0);
        venta.setCantidad(2);
        venta.setId_producto(1);

        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta saved = ventaService.save(venta);

        assertNotNull(saved);
        assertEquals(1, saved.getId_venta());
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    void testFindAll() {
        Venta venta = new Venta();
        venta.setId_venta(1);
        venta.setCosto(50.0);
        venta.setCantidad(2);
        venta.setId_producto(1);

        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<Venta> ventas = ventaService.findAll();

        assertNotNull(ventas);
        assertEquals(1, ventas.size());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        int id_venta = 1;
        Venta venta = new Venta();
        venta.setId_venta(id_venta);
        venta.setCosto(50.0);
        venta.setCantidad(2);
        venta.setId_producto(1);

        when(ventaRepository.findById(id_venta)).thenReturn(Optional.of(venta));

        Optional<Venta> found = ventaService.findById(id_venta);

        assertTrue(found.isPresent());
        assertEquals(venta, found.get());
        verify(ventaRepository, times(1)).findById(id_venta);
    }

    @Test
    void testDeleteById() {
        int id_venta = 1;
        doNothing().when(ventaRepository).deleteById(id_venta);

        ventaService.deleteById(id_venta);

        verify(ventaRepository, times(1)).deleteById(id_venta);
    }

    @Test
    void existsById_shouldReturnTrueIfExists() {
        when(ventaRepository.existsById(1)).thenReturn(true);

        boolean exists = ventaService.existsById(1);

        assertThat(exists).isTrue();
        verify(ventaRepository).existsById(1);
    }

    @Test
    void obtenerProducto_shouldReturnProductobyVentaResponse() {
        ProductobyVentaResponse response = new ProductobyVentaResponse();
        when(productoClient.getProductoById(5)).thenReturn(response);

        ProductobyVentaResponse result = ventaService.obtenerProducto(5);

        assertThat(result).isEqualTo(response);
        verify(productoClient).getProductoById(5);
    }

    @Test
    void findAllByIdProducto_shouldReturnVentasList() {
        Venta venta1 = new Venta();
        Venta venta2 = new Venta();
        when(ventaRepository.findAllByIdProducto(10)).thenReturn(List.of(venta1, venta2));

        List<Venta> ventas = ventaService.findAllByIdProducto(10);

        assertThat(ventas).containsExactly(venta1, venta2);
        verify(ventaRepository).findAllByIdProducto(10);
    }
}
