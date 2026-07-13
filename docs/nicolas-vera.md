# Defensa individual - Nicolas Vera

## Rol

Responsable de clases, reservas, equipamiento y API Gateway.

## Modulos asociados

- `class-service/class-service`
- `booking-service/booking-service`
- `equipment-service/equipment-service/equipment-service`
- `api-gateway`

## Funcionalidades principales

- CRUD de tipos de clase.
- Creacion de horarios con validacion de entrenador.
- Reserva y cancelacion de clases.
- Gestion de equipamiento, estado y mantenimiento.
- Comunicacion de booking-service con member-service y class-service.
- Ruteo centralizado, seguridad por roles y trazabilidad desde API Gateway.

## Pruebas asociadas

- `HorarioServiceImplTest`
- `ReservaServiceTest`
- `EquipoServiceTest`
- Pruebas de seguridad y ruteo del API Gateway.

## Endpoint para defender

`POST /api/bookings`: valida miembro, horario y reserva activa duplicada antes de crear una reserva.

## Relacion de base de datos para explicar

En `booking-service`, `Reserva` representa la relacion operativa entre un socio (`miembroId`) y un horario (`horarioId`). Como el socio vive en `member-service` y el horario vive en `class-service`, se guardan identificadores y se valida por clientes REST antes de persistir. En `class-service`, la relacion interna mas relevante es `TipoClase` 1:N `Horario`: una clase puede tener varios horarios, y cada horario pertenece a un tipo de clase.

## Dificultad tecnica personal

La dificultad mas importante fue coordinar reserva, cupos, cancelacion y entrada por Gateway sin duplicar datos de otros servicios. Se resolvio validando miembro y horario antes de reservar, bloqueando reservas activas duplicadas, liberando cupo al cancelar y configurando rutas `lb://` con seguridad por roles en el Gateway. Las pruebas cubren reserva exitosa, duplicidad, errores remotos, cancelacion y respuestas 401/403 del Gateway.

## Checklist

- [x] Codigo del servicio incluido.
- [x] Pruebas unitarias incluidas.
- [x] Swagger disponible.
- [x] Dockerfile incluido.
- [x] Evidencia individual documentada para defensa.
