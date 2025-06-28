package com.microservice.producto;

import com.microservice.producto.model.Producto;
import com.microservice.producto.repository.ProductoRepository;
import com.microservice.producto.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoRepository productoRepository;

    @Test
    void testSaveProducto() {
        Producto producto = Producto.builder()
                .codigo("ABC123")
                .nombre("Perfume Test")
                .marca("MarcaX")
                .fragancia("Floral")
                .genero("Mujer")
                .presentacionMl(100)
                .precio(99.99)
                .stock(10)
                .descripcion("Descripción de prueba")
                .build();

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto saved = productoService.save(producto);

        assertNotNull(saved);
        assertEquals("ABC123", saved.getCodigo());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    public void testFindAll() {
        Producto producto = Producto.builder()
        .id_producto(1)
        .codigo("ABC123")
        .nombre("Perfume Test")
        .marca("MarcaX")
        .fragancia("Floral")
        .genero("Mujer")
        .presentacionMl(100)
        .precio(99.99)
        .stock(10)
        .descripcion("Descripción de prueba")
        .build();

       
         when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> productos = productoService.findAll();

        assertNotNull(productos);
        assertEquals(1, productos.size());
        verify(productoRepository, times(1)).findAll();
    }

    
    @Test
    public void testGetProductById() {
        int id_producto = 1;
        Producto producto = Producto.builder()
                .id_producto(id_producto)
                .codigo("ABC123")
                .nombre("Perfume Test")
                .marca("MarcaX")
                .fragancia("Floral")
                .genero("Mujer")
                .presentacionMl(100)
                .precio(99.99)
                .stock(10)
                .descripcion("Descripción de prueba")
                .build();

        when(productoRepository.findById(id_producto)).thenReturn(Optional.of(producto));

        Optional<Producto> found = productoService.getProductbyId(id_producto);

        assertTrue(found.isPresent());
        assertEquals(producto, found.get());
        verify(productoRepository, times(1)).findById(id_producto);
    }

    @Test
    public void testGetProductById2() {
        int id_producto = 1;
        Producto producto = Producto.builder()
                .id_producto(id_producto)
                .codigo("ABC123")
                .nombre("Perfume Test")
                .marca("MarcaX")
                .fragancia("Floral")
                .genero("Mujer")
                .presentacionMl(100)
                .precio(99.99)
                .stock(10)
                .descripcion("Descripción de prueba")
                .build();

        when(productoRepository.findById(id_producto)).thenReturn(Optional.of(producto));

        Producto found = productoService.getProductById2(id_producto);

        assertNotNull(found);
        assertEquals(producto, found);
        verify(productoRepository, times(1)).findById(id_producto);
    }

    @Test
    public void testExistsByCodigo() {
        String codigo = "ABC123";
        when(productoRepository.existsByCodigo(codigo)).thenReturn(true);

        boolean exists = productoService.existsByCodigo(codigo);

        assertTrue(exists);
        verify(productoRepository, times(1)).existsByCodigo(codigo);
    }
    

    @Test
    public void testDeleteById(){
        int id_producto = 1;
        doNothing().when(productoRepository).deleteById(id_producto);
        
        productoService.delete(id_producto);
        
        verify(productoRepository, times(1)).deleteById(id_producto);

    }
}