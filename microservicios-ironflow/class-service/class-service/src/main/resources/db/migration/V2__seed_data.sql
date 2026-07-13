INSERT INTO tipos_clase (nombre, descripcion, estado) VALUES
('Spinning', 'Clase cardiovascular en bicicleta estatica', 'ACTIVO'),
('Funcional', 'Entrenamiento funcional grupal', 'ACTIVO');

INSERT INTO horarios (tipo_clase_id, entrenador_id, fecha_hora, aforo_max, aforo_actual, estado) VALUES
(1, 1, TIMESTAMP '2026-12-20 18:30:00', 20, 0, 'ACTIVO'),
(2, 2, TIMESTAMP '2026-12-21 19:00:00', 15, 0, 'ACTIVO');
