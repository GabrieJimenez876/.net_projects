-- Crear tabla Cliente
CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100),
    telefono VARCHAR(20)
);

-- Crear tabla Producto
CREATE TABLE Producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    precio DECIMAL(10, 2),
    stock INT
);

-- Crear tabla Empleado
CREATE TABLE Empleado (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    puesto VARCHAR(50)
);

-- Crear tabla Forma_Pago
CREATE TABLE Forma_Pago (
    id_forma_pago INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50)
);

-- Crear tabla Pedido
CREATE TABLE Pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    total DECIMAL(10, 2),
    id_cliente INT,
    id_empleado INT,
    id_forma_pago INT,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado),
    FOREIGN KEY (id_forma_pago) REFERENCES Forma_Pago(id_forma_pago)
);

-- Crear tabla Detalle_Pedido
CREATE TABLE Detalle_Pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT,
    precio_unitario DECIMAL(10, 2),
    id_producto INT,
    id_pedido INT,
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto),
    FOREIGN KEY (id_pedido) REFERENCES Pedido(id_pedido)
);
INSERT INTO Producto (nombre, precio, stock) VALUES
('Figura de Goku', 25.99, 10),
('Figura de Naruto', 20.50, 15),
('Poster de Attack on Titan', 5.99, 50),
('Figura de Luffy', 30.00, 8),
('Peluche de Pikachu', 12.00, 25),
('Camiseta de My Hero Academia', 18.50, 20),
('Taza de One Piece', 8.99, 30),
('Mochila de Sailor Moon', 22.00, 10),
('Sudadera de Dragon Ball Z', 35.00, 5),
('Figura de Sasuke', 21.99, 7),
-- Repetir algunos productos con variaciones o igual
('Figura de Goku', 25.99, 10),
('Figura de Naruto', 20.50, 15),
('Poster de Attack on Titan', 5.99, 50),
('Figura de Luffy', 30.00, 8),
('Peluche de Pikachu', 12.00, 25),
('Camiseta de My Hero Academia', 18.50, 20),
('Taza de One Piece', 8.99, 30),
('Mochila de Sailor Moon', 22.00, 10),
('Sudadera de Dragon Ball Z', 35.00, 5),
('Figura de Sasuke', 21.99, 7),
-- Continúa añadiendo más productos de anime...
('Reloj de Tokyo Ghoul', 14.50, 12),
('Póster de Fullmetal Alchemist', 6.50, 40),
('Gorra de Hunter x Hunter', 10.00, 22),
('Figura de Zoro', 29.99, 9),
('Peluche de Totoro', 15.99, 18);
INSERT INTO Cliente (nombre, email, telefono) VALUES
('Carlos Mendoza', 'carlos.mendoza@example.com', '71234567'),
('Sofía Pérez', 'sofia.perez@example.com', '70012345'),
('Luis Andrade', 'luis.andrade@example.com', '69987654'),
('Ana López', 'ana.lopez@example.com', '72233456'),
('Jorge Ramos', 'jorge.ramos@example.com', '71567890'),
('Fernanda Torres', 'fernanda.torres@example.com', '74125896'),
('José Torres', 'jose.torres@example.com', '74629581'),
('Lorena Cruz', 'lorena.cruz@example.com', '75932467'),
('Marcela Vega', 'marcela.vega@example.com', '72540987'),
('Elena Sánchez', 'elena.sanchez@example.com', '73219876'),
-- Repetimos algunos nombres y contactos
('Carlos Mendoza', 'carlos.mendoza@example.com', '71234567'),
('Sofía Pérez', 'sofia.perez@example.com', '70012345'),
('Luis Andrade', 'luis.andrade@example.com', '69987654'),
('Ana López', 'ana.lopez@example.com', '72233456'),
('Jorge Ramos', 'jorge.ramos@example.com', '71567890'),
('Fernanda Torres', 'fernanda.torres@example.com', '74125896'),
('José Torres', 'jose.torres@example.com', '74629581'),
('Lorena Cruz', 'lorena.cruz@example.com', '75932467'),
('Marcela Vega', 'marcela.vega@example.com', '72540987'),
('Elena Sánchez', 'elena.sanchez@example.com', '73219876'),
-- Añadimos más registros...
('Gabriela Flores', 'gabriela.flores@example.com', '74123698'),
('Ricardo Morales', 'ricardo.morales@example.com', '76543210'),
('Daniela Reyes', 'daniela.reyes@example.com', '75219834'),
('Alberto Gómez', 'alberto.gomez@example.com', '75876543'),
('Juan Carlos', 'juan.carlos@example.com', '74561234');
INSERT INTO Empleado (nombre, puesto) VALUES
('Miguel Ángel', 'Vendedor'),
('Patricia Ramírez', 'Gerente'),
('Laura Fernández', 'Cajera'),
('Diego Silva', 'Encargado de inventario'),
('Carla Muñoz', 'Vendedor'),
('Fernando Ortega', 'Cajero'),
('Jessica López', 'Vendedora'),
('Carlos Maldonado', 'Gerente'),
('Luis Villarroel', 'Asistente'),
('Maria José', 'Encargada de atención al cliente'),
-- Repetición de empleados
('Miguel Ángel', 'Vendedor'),
('Patricia Ramírez', 'Gerente'),
('Laura Fernández', 'Cajera'),
('Diego Silva', 'Encargado de inventario'),
('Carla Muñoz', 'Vendedor'),
('Fernando Ortega', 'Cajero'),
('Jessica López', 'Vendedora'),
('Carlos Maldonado', 'Gerente'),
('Luis Villarroel', 'Asistente'),
('Maria José', 'Encargada de atención al cliente'),
-- Agregar más empleados con algunas variaciones o repeticiones
('Sergio García', 'Vendedor'),
('Andrés Sánchez', 'Cajero'),
('Valeria Rodríguez', 'Vendedora'),
('Mónica Vargas', 'Encargada de inventario'),
('Roberto Álvarez', 'Gerente'),
-- Seguir el mismo patrón hasta completar 100 registros...
('Miguel Ángel', 'Vendedor'),
('Patricia Ramírez', 'Gerente'),
('Laura Fernández', 'Cajera'),
('Diego Silva', 'Encargado de inventario'),
('Carla Muñoz', 'Vendedor');
INSERT INTO Forma_Pago (tipo) VALUES
('Efectivo'),
('Tarjeta de crédito'),
('Tarjeta de débito'),
('Transferencia bancaria'),
('Paypal'),
('Efectivo'),
('Tarjeta de crédito'),
('Tarjeta de débito'),
('Transferencia bancaria'),
('Paypal'),
-- Repetir los tipos de pago para llenar 100 registros...
('Efectivo'),
('Tarjeta de crédito'),
('Tarjeta de débito'),
('Transferencia bancaria'),
('Paypal'),
('Efectivo'),
('Tarjeta de crédito'),
('Tarjeta de débito'),
('Transferencia bancaria'),
('Paypal'),
-- Continúa repitiendo hasta completar 100...
('Efectivo'),
('Tarjeta de crédito'),
('Tarjeta de débito'),
('Transferencia bancaria'),
('Paypal');
INSERT INTO Pedido (fecha, total, id_cliente, id_empleado, id_forma_pago) VALUES
('2024-01-10', 100.50, 1, 1, 1),
('2024-01-12', 55.00, 2, 2, 2),
('2024-01-15', 150.00, 3, 3, 3),
('2024-01-18', 80.75, 4, 4, 4),
('2024-01-20', 95.30, 5, 5, 5),
('2024-01-25', 120.00, 6, 6, 1),
('2024-01-28', 65.50, 7, 7, 2),
('2024-02-01', 75.00, 8, 8, 3),
('2024-02-05', 90.99, 9, 9, 4),
('2024-02-10', 200.00, 10, 10, 5),
-- Repetir pedidos con algunos datos repetidos o variaciones
('2024-02-15', 105.50, 1, 1, 1),
('2024-02-18', 55.00, 2, 2, 2),
('2024-02-20', 150.00, 3, 3, 3),
('2024-02-25', 80.75, 4, 4, 4),
('2024-02-28', 95.30, 5, 5, 5),
('2024-03-01', 120.00, 6, 6, 1),
('2024-03-05', 65.50, 7, 7, 2),
('2024-03-10', 75.00, 8, 8, 3),
('2024-03-15', 90.99, 9, 9, 4),
('2024-03-20', 200.00, 10, 10, 5),
-- Continúa repitiendo hasta completar 100 registros
('2024-03-25', 105.50, 1, 1, 1),
('2024-03-28', 55.00, 2, 2, 2),
('2024-04-01', 150.00, 3, 3, 3),
('2024-04-05', 80.75, 4, 4, 4),
('2024-04-10', 95.30, 5, 5, 5);

INSERT INTO Detalle_Pedido (cantidad, precio_unitario, id_producto, id_pedido) VALUES
(2, 25.99, 1, 1),
(1, 20.50, 2, 2),
(3, 5.99, 3, 3),
(1, 30.00, 4, 4),
(5, 12.00, 5, 5),
(2, 18.50, 6, 6),
(1, 8.99, 7, 7),
(3, 22.00, 8, 8),
(1, 35.00, 9, 9),
(2, 21.99, 10, 10),
-- Repetir detalles de pedidos con algunos datos repetidos
(2, 25.99, 1, 11),
(1, 20.50, 2, 12),
(3, 5.99, 3, 13),
(1, 30.00, 4, 14),
(5, 12.00, 5, 15),
(2, 18.50, 6, 16),
(1, 8.99, 7, 17),
(3, 22.00, 8, 18),
(1, 35.00, 9, 19),
(2, 21.99, 10, 20),
-- Continuar con repeticiones hasta alcanzar 100 registros
(2, 25.99, 1, 21),
(1, 20.50, 2, 22),
(3, 5.99, 3, 23),
(1, 30.00, 4, 24),
(5, 12.00, 5, 25);

