package com.venta.microservice_venta.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class VentaDTO {
    
    private int id_venta;
    private LocalDate fecha_venta;
    private LocalTime hora_venta;
    private Double costo;
    private Integer cantidad;
    private Double costoTotal;
    private int id_producto;

}
