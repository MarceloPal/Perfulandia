package com.microservice.producto.service;

import com.microservice.producto.model.Producto;
import com.microservice.producto.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    public List<Producto> findAll(){
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductbyId(int id_producto){
        return productoRepository.findById(id_producto);
    
    }

    public Producto getProductById2(int id){
        return productoRepository.findById(id).get();
    }

    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    public void delete(int id_producto){
        productoRepository.deleteById(id_producto);
    }
}
