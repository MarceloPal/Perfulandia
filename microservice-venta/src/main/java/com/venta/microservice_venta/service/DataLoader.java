package com.venta.microservice_venta.service;

import com.venta.microservice_venta.model.*;
import com.venta.microservice_venta.repository.VentaRepository;
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
    private VentaRepository ventaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Generar tipos de sala
        for (int i = 0; i < 3; i++) {
            Venta venta = new Venta();            
            producto.setId_venta(i + 1); 
            producto.setCodigo(faker.code().ean8()); 
            producto.setNombre(faker.commerce().productName()); 
            producto.setMarca(faker.company().name()); 
            producto.setFragancia(faker.commerce().material()); 
            producto.setGenero(faker.options().option("Masculino", "Femenino", "Unisex")); 
            
            ventaRepository.save(venta);
        }
        List<Producto> producto = productoRepository.findAll();

    }
}