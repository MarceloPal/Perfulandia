package com.microservice.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private int Id_producto;
    private String Codigo;
    private String Nombre;
    private String Marca;
    private String Fragancia;
    private String Genero;
    private int PresentacionMl;
    private double Precio;
    private int Stock;
    private String Descripcion;

}
