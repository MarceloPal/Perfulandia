package com.venta.microservice_venta;
import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.repository.VentaRepository; 

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Profile("test")
@Component

public class DataLoader implements CommandLineRunner {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 3; i++) {
            Venta venta = new Venta();

            venta.setFecha_venta(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));
            venta.setHora_venta(LocalTime.now().minusMinutes(faker.number().numberBetween(1, 60)));
            venta.setCosto(faker.number().randomDouble(2, 10, 100));
            venta.setCantidad(faker.number().numberBetween(1, 10));
            venta.setCostoTotal(
                venta.getCantidad() * venta.getCosto()
            );
            venta.setId_producto(i + 1); 
            ventaRepository.save(venta);
        }
    }

}
