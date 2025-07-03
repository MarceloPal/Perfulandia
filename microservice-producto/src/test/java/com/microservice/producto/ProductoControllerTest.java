package com.microservice.producto;

import com.microservice.producto.controller.ProductoController;
import com.microservice.producto.dto.ProductoDTO;
import com.microservice.producto.model.Producto;
import com.microservice.producto.service.ProductoService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Faker faker;
    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        
        producto = new Producto();
        producto.setId_producto(faker.number().numberBetween(1, 100));
        producto.setCodigo(faker.code().isbn10());
        producto.setNombre(faker.commerce().productName());
        producto.setMarca(faker.commerce().brand());
        producto.setFragancia(faker.lorem().word());
        producto.setGenero(faker.options().option("Masculino", "Femenino", "Unisex"));
        producto.setPresentacionMl(faker.number().numberBetween(50, 200));
        producto.setPrecio(faker.number().randomDouble(2, 20, 500));
        producto.setStock(faker.number().numberBetween(0, 100));
        producto.setDescripcion(faker.lorem().sentence());

        productoDTO = new ProductoDTO();
        productoDTO.setId_producto(producto.getId_producto());
        productoDTO.setCodigo(producto.getCodigo());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setMarca(producto.getMarca());
        productoDTO.setFragancia(producto.getFragancia());
        productoDTO.setGenero(producto.getGenero());
        productoDTO.setPresentacionMl(producto.getPresentacionMl());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        productoDTO.setDescripcion(producto.getDescripcion());
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        List<Producto> productos = Arrays.asList(producto, producto, producto);
        when(productoService.findAll()).thenReturn(productos);

        // Act
        List<Producto> result = productoController.getAllProducts();

        // Assert
        assertEquals(3, result.size());
        verify(productoService, times(1)).findAll();
    }

    @Test
    void testGetProductByIdFound() {
        // Arrange
        when(productoService.getProductbyId(producto.getId_producto())).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<?> response = productoController.getProductById(producto.getId_producto());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Producto);
        assertEquals(producto, response.getBody());
        verify(productoService, times(1)).getProductbyId(producto.getId_producto());
    }

    @Test
    void testGetProductByIdNotFound() {
        // Arrange
        int nonExistentId = faker.number().numberBetween(1000, 2000);
        when(productoService.getProductbyId(nonExistentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = productoController.getProductById(nonExistentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertEquals("No se encontro el producto con Id: " + nonExistentId, errorBody.get("message"));
        verify(productoService, times(1)).getProductbyId(nonExistentId);
    }

    @Test
    void testSaveProductSuccess() {
        // Arrange
        when(productoService.existsByCodigo(productoDTO.getCodigo())).thenReturn(false);
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<?> response = productoController.save(productoDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ProductoDTO);
        
        ProductoDTO responseDTO = (ProductoDTO) response.getBody();
        assertEquals(producto.getId_producto(), responseDTO.getId_producto());
        assertEquals(producto.getCodigo(), responseDTO.getCodigo());
        assertEquals(producto.getNombre(), responseDTO.getNombre());
        verify(productoService, times(1)).existsByCodigo(productoDTO.getCodigo());
        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    void testSaveProductWithDuplicateCode() {
        // Arrange
        when(productoService.existsByCodigo(productoDTO.getCodigo())).thenReturn(true);

        // Act
        ResponseEntity<?> response = productoController.save(productoDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals("Ya existe un producto con el código: " + productoDTO.getCodigo(), error.get("message"));
        verify(productoService, times(1)).existsByCodigo(productoDTO.getCodigo());
        verify(productoService, never()).save(any(Producto.class));
    }

    @Test
    void testSaveProductWithDataIntegrityViolation() {
        // Arrange
        when(productoService.existsByCodigo(productoDTO.getCodigo())).thenReturn(false);
        when(productoService.save(any(Producto.class))).thenThrow(DataIntegrityViolationException.class);

        // Act
        ResponseEntity<?> response = productoController.save(productoDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertTrue(error.get("message").contains("Ya existe un producto con el código"));
        verify(productoService, times(1)).existsByCodigo(productoDTO.getCodigo());
        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    void testUpdateProductSuccess() {
        // Arrange
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        // Act
        ResponseEntity<ProductoDTO> response = productoController.update(producto.getId_producto(), productoDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        ProductoDTO responseDTO = response.getBody();
        assertEquals(producto.getId_producto(), responseDTO.getId_producto());
        assertEquals(producto.getNombre(), responseDTO.getNombre());
        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    void testDeleteProductSuccess() {
        // Arrange
        doNothing().when(productoService).delete(producto.getId_producto());

        // Act
        ResponseEntity<?> response = productoController.eliminar(producto.getId_producto());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("El producto con ID: " + producto.getId_producto() + ", ha sido eliminado exitosamente.", 
                     responseBody.get("message"));
        verify(productoService, times(1)).delete(producto.getId_producto());
    }

    @Test
    void testDeleteProductNotFound() {
        // Arrange
        int nonExistentId = faker.number().numberBetween(1000, 2000);
        doThrow(new RuntimeException()).when(productoService).delete(nonExistentId);

        // Act
        ResponseEntity<?> response = productoController.eliminar(nonExistentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals("No se pudo eliminar el producto con ID: " + nonExistentId + ". Puede que no exista o haya un error en la base de datos.", 
                     error.get("message"));
        verify(productoService, times(1)).delete(nonExistentId);
    }

    @Test
    void update_shouldReturnNotFoundWhenExceptionIsThrown() {
        int id_producto = 123;
        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo("X");
        // ...setea otros campos necesarios...

        // Simula que productoService.save lanza una excepción
        when(productoService.save(any(Producto.class))).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<ProductoDTO> response = productoController.update(id_producto, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
     
}
