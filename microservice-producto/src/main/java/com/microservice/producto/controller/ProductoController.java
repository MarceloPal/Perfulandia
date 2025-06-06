package com.microservice.producto.controller;

import com.microservice.producto.model.Producto;
import com.microservice.producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.net.URI;
import java.time.LocalDateTime;
//import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/v1/productos")


public class ProductoController {

    @Autowired
    private ProductoService productoService;

    //PA LISTAR COMPLETA
    @GetMapping("/listar")
    public List<Producto> getAllProducts() {
        return productoService.findAll();
    }
    
    //MUESTRA OBJETO BUSCADO
    @GetMapping("/{id_producto}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id_producto) {

        Optional<Producto> producto = productoService.getProductbyId(id_producto);

        if(producto.isPresent()){
            return ResponseEntity.ok()
                        .header("mi-encabezado","valor")
                        .body(producto.get());
        }
        else{
            Map<String,String> errorBody = new HashMap<>();
            errorBody.put("message","No se encontro el producto con Id: " + id_producto);
            errorBody.put("status", "404");
            errorBody.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorBody);

        }
    }
    
    //PA GUARDAR
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody Producto producto){
        try {
            Producto productoGuardado = productoService.save(producto);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(productoGuardado.getId_producto())
                    .toUri();

            return ResponseEntity
                    .created(location)
                    .body(productoGuardado);
            
        } catch (DataIntegrityViolationException e) {
            Map<String,String> error = new HashMap<>();
            error.put("message", "El perfume fue registrado exitosamente.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }


    //PA EDITAR
    @PostMapping("/{id_producto}")
    public ResponseEntity<Producto> update(@PathVariable int id_producto,@RequestBody Producto producto) {

        try {
               Producto pro = productoService.getProductById2(id_producto);
                pro.setId_producto(id_producto);
                pro.setCodigo(producto.getCodigo());
                pro.setNombre(producto.getNombre());
                pro.setMarca(producto.getMarca());
                pro.setFragancia(producto.getFragancia());
                pro.setGenero(producto.getGenero());
                pro.setPresentacionMl(producto.getPresentacionMl());
                pro.setPrecio(producto.getPrecio());
                pro.setStock(producto.getStock());
                pro.setDescripcion(producto.getDescripcion());

                productoService.save(producto);
                return ResponseEntity.ok(producto);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
      
    }
    

   //Pa eliminar
   @DeleteMapping("/{id_producto}")
   public ResponseEntity<?> eliminar(@PathVariable int id_producto){
        try {
                productoService.delete(id_producto);
                return ResponseEntity.noContent().build();

            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }


   } 
}
