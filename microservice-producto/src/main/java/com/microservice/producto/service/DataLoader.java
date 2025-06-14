package com.microservice.producto.service;

import com.microservice.producto.model.*;
import com.microservice.producto.repository.ProductoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Generar tipos de sala
        for (int i = 0; i < 3; i++) {
            Producto producto = new Producto();            
            producto.setId_producto(i + 1); 
            producto.setCodigo(faker.code().ean8()); 
            producto.setNombre(faker.commerce().productName()); 
            producto.setMarca(faker.company().name()); 
            producto.setFragancia(faker.commerce().material()); 
            producto.setGenero(faker.options().option("Masculino", "Femenino", "Unisex")); 
            producto.setPresentacionMl(faker.number().numberBetween(30, 500)); 
            producto.setDescripcion(faker.lorem().sentence()); 
            producto.setStock(faker.number().numberBetween(1, 200)); 
            producto.setPrecio(faker.number().randomDouble(2, 1000, 50000));
            
            productoRepository.save(producto);
        }
        List<Producto> producto = productoRepository.findAll();

    }
}