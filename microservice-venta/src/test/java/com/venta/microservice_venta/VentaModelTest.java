package com.venta.microservice_venta;
import com.venta.microservice_venta.model.Venta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VentaModelTest {

    @Test
    void calcularCostoTotal_asignaCostoTotalCorrectamente() {
        Venta venta = new Venta();
        venta.setCosto(100.0);
        venta.setCantidad(3);

        venta.calcularCostoTotal();

        assertEquals(300.0, venta.getCostoTotal());
    }

    @Test
    void calcularCostoTotal_lanzaExcepcionSiCostoEsNull() {
        Venta venta = new Venta();
        venta.setCantidad(2);
        venta.setCosto(null);

        Exception exception = assertThrows(IllegalStateException.class, venta::calcularCostoTotal);
        assertEquals("Costo y cantidad deben estar definidos", exception.getMessage());
    }

    @Test
    void calcularCostoTotal_lanzaExcepcionSiCantidadEsNull() {
        Venta venta = new Venta();
        venta.setCosto(50.0);
        venta.setCantidad(null);

        Exception exception = assertThrows(IllegalStateException.class, venta::calcularCostoTotal);
        assertEquals("Costo y cantidad deben estar definidos", exception.getMessage());
    }
}
