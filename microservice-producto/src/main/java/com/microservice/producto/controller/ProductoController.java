package com.microservice.producto.controller;

import com.microservice.producto.model.Producto;
import com.microservice.producto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.microservice.producto.dto.ProductoDTO;
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
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.net.URI;
import java.time.LocalDateTime;

//Se crean las peticiones REST para el microservicio de productos

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name="Productos",description = "Operaciones relacionadas con los productos del sistema")

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    //Lista todos los productos
    @Operation(summary = "Obtener todos los productos",description = "Obtiene una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Operación exitosa")
    @GetMapping("/listar")
    public List<Producto> getAllProducts() {
        return productoService.findAll();
    }
    

    //Estas notaciones se utilizan para documentar mejor los endpoints rest del codigo
    //de forma que swagger genere una documentacion mas clara.
    //Muestra un producto por ID
     
    @Operation(summary = "Obtener producto por ID", description = "Busca un producto por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Parameter(name = "id_producto", description = "ID del producto", required = true)
    @GetMapping("/{id_producto}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id_producto) {

        Optional<Producto> producto = productoService.getProductbyId(id_producto);

        if(producto.isPresent()){

            ProductoDTO dto = new ProductoDTO();
            dto.setId_producto(producto.get().getId_producto());
            dto.setCodigo(producto.get().getCodigo());
            dto.setNombre(producto.get().getNombre());
            dto.setMarca(producto.get().getMarca());
            dto.setFragancia(producto.get().getFragancia());
            dto.setGenero(producto.get().getGenero());
            dto.setPresentacionMl(producto.get().getPresentacionMl());
            dto.setPrecio(producto.get().getPrecio());
            dto.setStock(producto.get().getStock());
            dto.setDescripcion(producto.get().getDescripcion());


            return ResponseEntity.ok()
                        .header("mi-encabezado","valor").body(producto.get());
        }
        else{
            Map<String,String> errorBody = new HashMap<>();
            errorBody.put("message","No se encontro el producto con Id: " + id_producto);
            errorBody.put("status", "404");
            errorBody.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);

        }
    }
    
    //Este método guarda un nuevo producto
    //tuve que agregar un if para que no se repita el código del producto
@Operation(summary = "Crear un producto", description = "Crea un nuevo producto")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "409", description = "Conflicto: código de producto duplicado o error de integridad")
    })
  @PostMapping
  public ResponseEntity<?> save(@Valid @RequestBody ProductoDTO productoDTO){
    try {
        if (productoService.existsByCodigo(productoDTO.getCodigo())) {
            Map<String,String> error = new HashMap<>();
            error.put("message", "Ya existe un producto con el código: " + productoDTO.getCodigo());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        Producto producto = new Producto();
        producto.setCodigo(productoDTO.getCodigo());
        producto.setNombre(productoDTO.getNombre());
        producto.setMarca(productoDTO.getMarca());
        producto.setFragancia(productoDTO.getFragancia());
        producto.setGenero(productoDTO.getGenero());
        producto.setPresentacionMl(productoDTO.getPresentacionMl());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setDescripcion(productoDTO.getDescripcion());

        Producto productoGuardado = productoService.save(producto);

        ProductoDTO responseDTO = new ProductoDTO();
        responseDTO.setId_producto(productoGuardado.getId_producto());
        responseDTO.setCodigo(productoGuardado.getCodigo());
        responseDTO.setNombre(productoGuardado.getNombre());
        responseDTO.setMarca(productoGuardado.getMarca());
        responseDTO.setFragancia(productoGuardado.getFragancia());
        responseDTO.setGenero(productoGuardado.getGenero());
        responseDTO.setPresentacionMl(productoGuardado.getPresentacionMl());
        responseDTO.setPrecio(productoGuardado.getPrecio());
        responseDTO.setStock(productoGuardado.getStock());
        responseDTO.setDescripcion(productoGuardado.getDescripcion());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(productoGuardado.getId_producto()).toUri();

        return ResponseEntity.created(location).body(responseDTO);

    } catch(DataIntegrityViolationException e){
            Map<String,String> error = new HashMap<>();
            error.put("message","Ya existe un producto con el código: " + productoDTO.getCodigo() + " o hay un error de integridad referencial.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
}


    //Edita un producto por ID
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Parameter(name = "id_producto", description = "ID del producto", required = true)
    @PutMapping("/{id_producto}")
    public ResponseEntity<ProductoDTO> update(@PathVariable int id_producto,@RequestBody ProductoDTO productoDTO) {

        try {
              
            Producto producto = new Producto();
            producto.setId_producto(id_producto);
            producto.setCodigo(productoDTO.getCodigo());
            producto.setNombre(productoDTO.getNombre());
            producto.setMarca(productoDTO.getMarca());
            producto.setFragancia(productoDTO.getFragancia());
            producto.setGenero(productoDTO.getGenero());
            producto.setPresentacionMl(productoDTO.getPresentacionMl());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setStock(productoDTO.getStock());
            producto.setDescripcion(productoDTO.getDescripcion());

            Producto proEditado = productoService.save(producto);

            ProductoDTO responseDTO = new ProductoDTO();
            responseDTO.setId_producto(proEditado.getId_producto());
            responseDTO.setCodigo(proEditado.getCodigo());
            responseDTO.setNombre(proEditado.getNombre());
            responseDTO.setMarca(proEditado.getMarca());
            responseDTO.setFragancia(proEditado.getFragancia());
            responseDTO.setGenero(proEditado.getGenero());
            responseDTO.setPresentacionMl(proEditado.getPresentacionMl());
            responseDTO.setPrecio(proEditado.getPrecio());
            responseDTO.setStock(proEditado.getStock());
            responseDTO.setDescripcion(proEditado.getDescripcion());

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            Map<String,String> error = new HashMap<>();
            error.put("message", "No se pudo actualizar el producto con ID: " + id_producto + ". Puede que no exista o haya un error en la base de datos.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
      
    }
    

   //Elimina un producto por ID
   @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Parameter(name = "id_producto", description = "ID del producto", required = true)
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
