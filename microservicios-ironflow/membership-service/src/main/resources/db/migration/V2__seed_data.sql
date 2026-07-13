INSERT INTO planes (nombre, precio, dias_duracion, estado) VALUES
('Plan Mensual', 29990, 30, 'ACTIVO'),
('Plan Trimestral', 79990, 90, 'ACTIVO'),
('Plan Anual', 249990, 365, 'ACTIVO');

INSERT INTO planes_miembros (miembro_id, plan_id, fecha_inicio, fecha_fin, estado, creado_en) VALUES
(1, 1, DATE '2026-07-10', DATE '2026-08-09', 'ACTIVO', TIMESTAMP '2026-07-10 09:00:00'),
(2, 2, DATE '2026-07-10', DATE '2026-10-08', 'ACTIVO', TIMESTAMP '2026-07-10 09:30:00');
