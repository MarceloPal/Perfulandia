package com.venta.microservice_venta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "venta")
@AllArgsConstructor
@RequiredArgsConstructor
@Data

public class venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_venta;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha_venta;
    
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora_venta;

    @Column(nullable = false)
    @Positive
    private Double costo;

    private int id_producto;
}
