package com.microservice.producto;

import com.microservice.producto.controller.ProductoControllerv2;
import com.microservice.producto.dto.ProductoDTO;
import com.microservice.producto.model.Producto;
import com.microservice.producto.service.ProductoService;
import com.microservice.producto.assemblers.ProductoModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductoControllerv2Test {

    @InjectMocks
    private ProductoControllerv2 controller;

    @Mock
    private ProductoService productoService;

    @Mock
    private ProductoModelAssembler assembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductById_found() {
        Producto producto = new Producto();
        producto.setId_producto(1);
        EntityModel<Producto> entityModel = EntityModel.of(producto);

        when(productoService.getProductbyId(1)).thenReturn(Optional.of(producto));
        when(assembler.toModel(producto)).thenReturn(entityModel);

        ResponseEntity<?> response = controller.getProductById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(entityModel);
    }

    @Test
    void getProductById_notFound() {
        when(productoService.getProductbyId(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getProductById(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> error = (Map<?, ?>) response.getBody();
        assertThat(error.get("message")).isEqualTo("No se encontró el producto con Id: 99");
    }

    @Test
    void all_returnsCollectionModel() {
        Producto producto = new Producto();
        producto.setId_producto(1);
        EntityModel<Producto> entityModel = EntityModel.of(producto);
        List<EntityModel<Producto>> list = List.of(entityModel);
        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(list);

        when(productoService.findAll()).thenReturn(List.of(producto));
        when(assembler.toModel(producto)).thenReturn(entityModel);

        CollectionModel<EntityModel<Producto>> result = controller.all();

        assertThat(result.getContent()).contains(entityModel);
    }

    @Test
    void save_createsProducto() {
        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo("ABC123");
        Producto producto = new Producto();
        producto.setCodigo("ABC123");
        Producto saved = new Producto();
        saved.setId_producto(1);
        saved.setCodigo("ABC123");
        EntityModel<Producto> entityModel = EntityModel.of(saved);

        when(productoService.existsByCodigo("ABC123")).thenReturn(false);
        when(productoService.save(any(Producto.class))).thenReturn(saved);
        when(assembler.toModel(saved)).thenReturn(entityModel);

        ResponseEntity<?> response = controller.save(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(entityModel);
    }

    @Test
    void save_conflictOnDuplicateCodigo() {
        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo("DUPLICADO");

        when(productoService.existsByCodigo("DUPLICADO")).thenReturn(true);

        ResponseEntity<?> response = controller.save(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> error = (Map<?, ?>) response.getBody();
        assertThat(error.get("message")).isEqualTo("Ya existe un producto con el código: DUPLICADO");
    }

    @Test
    void update_found() {
        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo("EDIT");
        Producto existente = new Producto();
        existente.setId_producto(2);
        Producto actualizado = new Producto();
        actualizado.setId_producto(2);
        actualizado.setCodigo("EDIT");
        EntityModel<Producto> entityModel = EntityModel.of(actualizado);

        when(productoService.getProductbyId(2)).thenReturn(Optional.of(existente));
        when(productoService.save(any(Producto.class))).thenReturn(actualizado);
        when(assembler.toModel(actualizado)).thenReturn(entityModel);

        ResponseEntity<?> response = controller.update(2, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(entityModel);
    }

    @Test
    void update_notFound() {
        ProductoDTO dto = new ProductoDTO();
        when(productoService.getProductbyId(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.update(99, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> error = (Map<?, ?>) response.getBody();
        assertThat(error.get("message")).isEqualTo("No se encontró el producto con Id: 99");
    }

    @Test
    void delete_found() {
        Producto producto = new Producto();
        producto.setId_producto(3);

        when(productoService.getProductbyId(3)).thenReturn(Optional.of(producto));
        doNothing().when(productoService).delete(3);

        ResponseEntity<?> response = controller.delete(3);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Producto eliminado con éxito");
    }

    @Test
    void delete_notFound() {
        when(productoService.getProductbyId(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.delete(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> error = (Map<?, ?>) response.getBody();
        assertThat(error.get("message")).isEqualTo("No se encontró el producto con Id: 99");
    }
}