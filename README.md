EndPoints de Productos:

GET localhost:8080/api/v1/productos/listar 

POST localhost:8080/api/v1/productos

PUT localhost:8080/api/v1/productos/{id}

DELETE localhost:8080/api/v1/productos/{id}

Endpoints de Venta:

GET localhost:8080/api/v1/ventas/listar

GET localhost:8080/api/v1/ventas/listar{id} (busca por id)

POST localhost:8080/api/v1/ventas

{
  "fecha_venta": "2025-06-08",
  "hora_venta": "15:45:00",
  "costo": 75.0,
  "cantidad": 2,
  "id_producto": 1
}


DELETE localhost:8080/api/v1/ventas/{id}

Valores para la base de datos:

INSERT INTO producto (
    codigo, nombre, marca, fragancia, genero, presentacion_ml, precio, stock, descripcion
) VALUES 
('P12345', 'Euphoria', 'Calvin Klein', 'Amaderada', 'Mujer', 100, 75.50, 50, 'Fragancia intensa y duradera');

INSERT INTO producto (
    codigo, nombre, marca, fragancia, genero, presentacion_ml, precio, stock, descripcion
) VALUES 
('P67890', 'Bleu', 'Chanel', 'Aromática', 'Hombre', 50, 89.99, 30, 'Fresca con notas cítricas y especiadas');

INSERT INTO producto (
    codigo, nombre, marca, fragancia, genero, presentacion_ml, precio, stock, descripcion
) VALUES 
('P54321', 'Light Blue', 'Dolce & Gabbana', 'Cítrica', 'Unisex', 75, 65.00, 20, 'Ligera y fresca para el verano');
