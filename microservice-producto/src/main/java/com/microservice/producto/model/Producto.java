package com.microservice.producto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;


@Entity
@Table(name = "producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_producto;

    @Column(name="codigo", unique=true,length=6,nullable = false)
    @NotBlank(message = "El código no puede estar vacío")
    @Size(min=5,max=8)
    private String codigo;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "La marca no puede estar vacía")
    private String marca;
    
    @Column(nullable = false)
    @NotBlank(message = "La fragancia no puede estar vacía")
    private String fragancia;

    @Pattern(regexp = "Hombre|Mujer|Unisex", message = "El género debe ser Hombre, Mujer o Unisex")
    private String genero;

    @Min(1)
    private int presentacionMl;

    @DecimalMin(value = "0.0", inclusive = false)
    private double precio;

    @Min(0)
    private int stock;


    @Size(max = 255)
    private String descripcion;

     @ManyToMany
    @JoinTable( 
        name="producto_venta"
        ,joinColumns= @JoinColumn(name="id_producto")
        ,inverseJoinColumns = @JoinColumn(name="id_venta")
    )
    
    private Set<Producto> producto; // Se ocupa Set pq es muchos a muchos, y no se repiten los productos en una venta
    

}
