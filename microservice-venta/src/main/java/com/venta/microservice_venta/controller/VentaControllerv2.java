package com.venta.microservice_venta.controller;

import com.venta.microservice_venta.model.Venta;
import com.venta.microservice_venta.service.VentaService;
import com.venta.microservice_venta.DTO.VentaDTO;
import com.venta.microservice_venta.assemblers.VentaModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/ventas")
public class VentaControllerv2 {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Venta>> all() {
        List<EntityModel<Venta>> ventas = ventaService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ventas,
                linkTo(methodOn(VentaControllerv2.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVentaById(@PathVariable int id) {
        Optional<Venta> venta = ventaService.findById(id);
        if (venta.isPresent()) {
            return ResponseEntity.ok(assembler.toModel(venta.get()));
        } else {
            // Respuesta de error estructurada
            var error = new java.util.HashMap<String, Object>();
            error.put("message", "Venta no encontrada");
            error.put("status", 404);
            error.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
}

    @PostMapping
    public ResponseEntity<?> save(@RequestBody VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setFecha_venta(ventaDTO.getFecha_venta());
        venta.setHora_venta(ventaDTO.getHora_venta());
        venta.setCosto(ventaDTO.getCosto());
        venta.setCantidad(ventaDTO.getCantidad());
        venta.setId_producto(ventaDTO.getId_producto());

        Venta ventaGuardada = ventaService.save(venta);
        EntityModel<Venta> ventaModel = assembler.toModel(ventaGuardada);

        URI location = linkTo(methodOn(VentaControllerv2.class).getVentaById(ventaGuardada.getId_venta())).toUri();

        return ResponseEntity.created(location).body(ventaModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!ventaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada");
        }
        ventaService.deleteById(id);
        return ResponseEntity.ok().body("Venta eliminada");
    }
}
