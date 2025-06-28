package com.venta.microservice_venta;

import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.repository.VentaRepository;
import com.venta.microservice_venta.service.VentaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @InjectMocks
    private VentaService ventaService;

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
}