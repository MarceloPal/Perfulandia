package com.microservice.producto.model;

//import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id_producto;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Se debe rellenar el espacio")
    @Size(min=5,max=8)
    private String Codigo;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String Nombre;

    @Column(nullable = false)
    @NotBlank(message = "La marca no puede estar vacía")
    private String Marca;

    private String Fragancia;

    @Pattern(regexp = "Hombre|Mujer|Unisex", message = "El género debe ser Hombre, Mujer o Unisex")
    private String Genero;

    @Min(1)
    private int PresentacionMl;

    @DecimalMin(value = "0.0", inclusive = false)
    private double Precio;

    @Min(0)
    private int Stock;


    @Size(max = 255)
    private String Descripcion;

}
