package com.venta.microservice_venta.BO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class VentaBO {

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha_venta;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora_venta;
    private Double costo;
    private Integer id_producto;
    private String comentario;

}
