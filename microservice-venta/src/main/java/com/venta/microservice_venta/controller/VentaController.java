package com.venta.microservice_venta.controller;

import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.service.VentaService;
import com.venta.microservice_venta.DTO.VentaDTO;
import com.venta.microservice_venta.http.response.ProductobyVentaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

//import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.net.URI;


@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Lista las ventas
    @GetMapping("/producto/{id_producto}")
    public ResponseEntity<ProductobyVentaResponse> getProductoDeVenta(@PathVariable int id_producto) {
    ProductobyVentaResponse producto = ventaService.obtenerProducto(id_producto);
    return ResponseEntity.ok(producto);
}

    // Crear una venta
    // Debe ser manual en postman, sin incluir el costototal, ya que se calcula automáticamente
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody VentaDTO ventaDTO) {

        try {
            Venta venta = new Venta();
            venta.setFecha_venta(ventaDTO.getFecha_venta());
            venta.setHora_venta(ventaDTO.getHora_venta());
            venta.setCosto(ventaDTO.getCosto());
            venta.setCantidad(ventaDTO.getCantidad());
            venta.setId_producto(ventaDTO.getId_producto());
           
            Venta ventaGuardada = ventaService.save(venta);

            VentaDTO responseDTO = new VentaDTO();
            responseDTO.setId_venta(ventaGuardada.getId_venta());
            responseDTO.setFecha_venta(ventaGuardada.getFecha_venta());
            responseDTO.setHora_venta(ventaGuardada.getHora_venta());
            responseDTO.setCosto(ventaGuardada.getCosto());
            responseDTO.setCantidad(ventaGuardada.getCantidad());
            responseDTO.setCostoTotal(ventaGuardada.getCostoTotal());
            responseDTO.setId_producto(ventaGuardada.getId_producto());

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(ventaGuardada.getId_venta()).toUri();

            return ResponseEntity.created(location).body(responseDTO);

        } catch (DataIntegrityViolationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La Boleta ya existe o hay un error de integridad referencial.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

    
    }
    
    //Bucar alguna venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable int id) {
        Optional<Venta> venta = ventaService.findById(id);
        return venta.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }


    //Elimina alguna venta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable int id) {
    try {
        if (!ventaService.existsById(id)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "No se encontró la venta con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        ventaService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "La venta con ID: " + id + " ha sido eliminada exitosamente.");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "No se pudo eliminar la venta con ID: " + id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
}

