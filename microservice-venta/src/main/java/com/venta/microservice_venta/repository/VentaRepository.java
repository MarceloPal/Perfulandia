package com.venta.microservice_venta.repository;

import com.venta.microservice_venta.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta,Integer>{
    
        @Query("SELECT v FROM Venta v WHERE v.id_producto = :id_producto")
        List<Venta> findAllByIdProducto(@Param("id_producto") int id_producto);
}