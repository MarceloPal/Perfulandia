package com.venta.microservice_venta.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class VentaDTO {

    private int id_venta;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha_venta;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora_venta;
    private Double costo;
    private Integer cantidad;
    private Double costoTotal;
    private Integer id_producto;
    

}
