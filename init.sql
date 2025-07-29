-- Script de inicialización de la base de datos
-- Crear la tabla Producto

CREATE TABLE IF NOT EXISTS Producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    descripcion TEXT,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_producto_nombre ON Producto(nombre);
CREATE INDEX idx_producto_precio ON Producto(precio);
CREATE INDEX idx_producto_fecha_creacion ON Producto(fecha_creacion);

-- Insertar algunos productos de ejemplo
INSERT INTO Producto (nombre, precio, descripcion) VALUES
('Laptop Dell Inspiron 15', 1500.00, 'Laptop Dell Inspiron 15 pulgadas con procesador Intel i5'),
('Mouse Logitech MX Master', 89.99, 'Mouse inalámbrico Logitech MX Master 3'),
('Teclado Mecánico Corsair', 129.99, 'Teclado mecánico Corsair K70 RGB'),
('Monitor Samsung 27"', 299.99, 'Monitor Samsung 27 pulgadas Full HD'),
('Auriculares Sony WH-1000XM4', 349.99, 'Auriculares inalámbricos Sony con cancelación de ruido');

-- Verificar que la tabla se creó correctamente
SELECT 'Tabla Producto creada exitosamente' as mensaje;
SELECT COUNT(*) as total_productos FROM Producto;