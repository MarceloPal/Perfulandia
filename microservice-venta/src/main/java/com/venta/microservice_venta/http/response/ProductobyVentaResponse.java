package com.venta.microservice_venta.http.response;


import lombok.Data;

@Data
public class ProductobyVentaResponse {
    private int id_producto;
    private String codigo;
    private String nombre;
    private String marca;
    private String fragancia;
    private String genero;
    private Integer presentacionMl;
    private Double precio;
    private Integer stock;
    private String descripcion;

    //Acá generamos un transporte de los datos del producto
    //al microservicio de venta, facilitando la comnicacion.
}