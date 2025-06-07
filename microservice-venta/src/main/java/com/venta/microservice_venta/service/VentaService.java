package com.venta.microservice_venta.service;

import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.repository.VentaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> findById(int id) {
        return ventaRepository.findById(id);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public boolean existsById(int id) {
        return ventaRepository.existsById(id);
    }

    public List<Venta> findAllByIdProducto(int idProducto) {
        return ventaRepository.findAllByIdProducto(idProducto);
    }
    public void deleteById(int id) {
        ventaRepository.deleteById(id);
    }
}