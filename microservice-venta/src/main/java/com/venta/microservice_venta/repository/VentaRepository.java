package com.venta.microservice_venta.repository;

import com.venta.microservice_venta.model.venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<venta,Integer>{
    
    @Query("SELECT a FROM atencion a WHERE a.id_producto = :id_producto")
    List<venta> findAllAtention(int id_producto);
}