package com.venta.microservice_venta.service;

import com.venta.microservice_venta.model.venta;
import com.venta.microservice_venta.repository.VentaRepository;
import com.venta.microservice_venta.BO.VentaBO;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository VentaRepository;

    public boolean existeAtencion(int id){
        return VentaRepository.existsById(id);
    }

    public List<venta> findAll(){
        return VentaRepository.findAll();
    }

    public Optional<venta> getPatientById(int id){
        return VentaRepository.findById(id);
    }

    public venta getPatientById2(int id){
        return VentaRepository.findById(id).get();
    }
    
}