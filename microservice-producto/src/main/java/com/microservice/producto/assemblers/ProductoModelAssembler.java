package com.microservice.producto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.microservice.producto.controller.ProductoControllerv2;
import com.microservice.producto.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    //En entitymodel se envuelve el producto y se le agregan enlaces hateoas, esto es un recurso hateoas.
    @Override
    public EntityModel<Producto> toModel(Producto producto) {

        //retorno dos enlaces HATEOAS para el producto
        //1. Enlace al propio producto (self)   
        //2. Enlace a la lista de productos (all)
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoControllerv2.class).getProductById(producto.getId_producto())).withSelfRel(),
            linkTo(methodOn(ProductoControllerv2.class).all()).withRel("productos")
        );
    }
}




//Convierte una instancia de producto en un objeto con enlaces HATEOAS.
//Osea, un recurso restful, que incluye hipervinculos navegales.