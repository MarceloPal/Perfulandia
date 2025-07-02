package com.venta.microservice_venta.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.venta.microservice_venta.controller.VentaControllerv2;
import com.venta.microservice_venta.model.Venta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<Venta, EntityModel<Venta>> {

    @Override
    public EntityModel<Venta> toModel(Venta venta) {
        return EntityModel.of(venta,
            linkTo(methodOn(VentaControllerv2.class).getVentaById(venta.getId_venta())).withSelfRel(),
            linkTo(methodOn(VentaControllerv2.class).all()).withRel("ventas")
        );
    }

}
