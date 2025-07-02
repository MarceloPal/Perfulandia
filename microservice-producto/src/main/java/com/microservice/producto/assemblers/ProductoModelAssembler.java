package com.microservice.producto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.microservice.producto.controller.ProductoControllerv2;
import com.microservice.producto.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoControllerv2.class).getProductById(producto.getId_producto())).withSelfRel(),
            linkTo(methodOn(ProductoControllerv2.class).all()).withRel("productos")
        );
    }
}
