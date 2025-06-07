package com.microservice.producto.controller;

import com.microservice.producto.model.Producto;
import com.microservice.producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/api/v1/productos")


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
    @PostMapping
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
            error.put("message", "El perfume estás registrado.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }


    //PA EDITAR
    @PutMapping("/{id_producto}")
    public ResponseEntity<?> update(@PathVariable int id_producto,@RequestBody Producto producto) {

        try {
               Producto proEditado = productoService.getProductById2(id_producto);
                proEditado.setCodigo(producto.getCodigo());
                proEditado.setNombre(producto.getNombre());
                proEditado.setMarca(producto.getMarca());
                proEditado.setFragancia(producto.getFragancia());
                proEditado.setGenero(producto.getGenero());
                proEditado.setPresentacionMl(producto.getPresentacionMl());
                proEditado.setPrecio(producto.getPrecio());
                proEditado.setStock(producto.getStock());
                proEditado.setDescripcion(producto.getDescripcion());

                Producto proActualizado = productoService.save(proEditado);
                return ResponseEntity.ok(proActualizado);

        } catch (Exception e) {
            Map<String,String> error = new HashMap<>();
            error.put("message", "No se pudo actualizar el producto con ID: " + id_producto + ". Puede que no exista o haya un error en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
      
    }
    

   //Pa eliminar
   @DeleteMapping("/{id_producto}")
   public ResponseEntity<?> eliminar(@PathVariable int id_producto){
        try {
                productoService.delete(id_producto);
                 Map<String,String> response = new HashMap<>();
                response.put("message","El producto con ID: " + id_producto + ", ha sido eliminado exitosamente."); 
                return ResponseEntity.ok(response);

            
        } catch (Exception e) {
            Map<String,String> error = new HashMap<>();
            error.put("message", "No se pudo eliminar el producto con ID: " + id_producto + ". Puede que no exista o haya un error en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }


   } 
}
