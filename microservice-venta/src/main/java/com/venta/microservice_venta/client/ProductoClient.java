package com.venta.microservice_venta.client;


import com.venta.microservice_venta.http.response.ProductobyVentaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 // Delara un cliente Feign llamado msvc-producto que se conecta al servicio de productos
@FeignClient(name = "msvc-producto", url = "http://localhost:8090/api/v1/productos")
public interface ProductoClient {

    @GetMapping("/{id_producto}")
    ProductobyVentaResponse getProductoById(@PathVariable("id_producto") int id_producto);

    //Acá define un método para hacer una solcitud GET al servicio de productos
    //y retorna un objeto ProductobyVentaResponse con los datos obtenidos.
}