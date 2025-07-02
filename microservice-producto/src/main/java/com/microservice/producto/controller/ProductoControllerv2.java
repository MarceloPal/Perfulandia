package com.microservice.producto.controller;

import com.microservice.producto.dto.ProductoDTO;
import com.microservice.producto.model.Producto;
import com.microservice.producto.service.ProductoService;
import com.microservice.producto.assemblers.ProductoModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;



import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/productos")
public class ProductoControllerv2 {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Producto>> all() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
            .map(assembler::toModel)
            .toList();

        return CollectionModel.of(productos,
            linkTo(methodOn(ProductoControllerv2.class).all()).withSelfRel());
    }

    @GetMapping("/{id_producto}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id_producto) {
        Optional<Producto> producto = productoService.getProductbyId(id_producto);

        if (producto.isPresent()) {
            // Devuelve el recurso HATEOAS
            return ResponseEntity.ok(assembler.toModel(producto.get()));
        } else {
            // Devuelve error detallado
            Map<String, Object> error = new HashMap<>();
            error.put("message", "No se encontró el producto con Id: " + id_producto);
            error.put("status", 404);
            error.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    //Se cambio para no ocupar servletUriComponentsBuilder
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProductoDTO productoDTO) {
        if (productoService.existsByCodigo(productoDTO.getCodigo())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Ya existe un producto con el código: " + productoDTO.getCodigo());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        Producto producto = dtoToEntity(productoDTO);
        Producto guardado = productoService.save(producto);
        EntityModel<Producto> entityModel = assembler.toModel(guardado);

        // Devuelve el recurso creado con status 201 (CREATED), sin Location
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id_producto}")
    public ResponseEntity<?> update(@PathVariable Integer id_producto, @RequestBody ProductoDTO productoDTO) {
        Optional<Producto> existente = productoService.getProductbyId(id_producto);

        if (existente.isPresent()) {
            Producto producto = dtoToEntity(productoDTO);
            producto.setId_producto(id_producto);

            Producto actualizado = productoService.save(producto);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "No se encontró el producto con Id: " + id_producto);
            error.put("status", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/{id_producto}")
    public ResponseEntity<?> delete(@PathVariable Integer id_producto) {
        Optional<Producto> producto = productoService.getProductbyId(id_producto);

        if (producto.isPresent()) {
            productoService.delete(id_producto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Producto eliminado con éxito");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "No se encontró el producto con Id: " + id_producto);
            error.put("status", "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Método auxiliar para convertir de DTO a entidad
    private Producto dtoToEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setMarca(dto.getMarca());
        producto.setFragancia(dto.getFragancia());
        producto.setGenero(dto.getGenero());
        producto.setPresentacionMl(dto.getPresentacionMl());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setDescripcion(dto.getDescripcion());
        return producto;
    }
}
