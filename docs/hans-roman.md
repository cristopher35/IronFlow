# Defensa individual - Hans Roman

## Rol

Responsable de servicios de entrenadores, acceso, sucursales y notificaciones.

## Modulos asociados

- `trainer-service/trainer-service`
- `access-service/access-service`
- `branch-service/branch-service`
- `notification-service/notification-service`

## Funcionalidades principales

- CRUD y baja logica de entrenadores.
- Busqueda de entrenadores por especialidad.
- Registro y verificacion de accesos.
- Consulta de accesos por socio.
- CRUD y busqueda de sucursales por ciudad.
- Registro y consulta de notificaciones.
- Validacion remota de socio antes de registrar notificaciones.

## Pruebas asociadas

- `EntrenadorServiceTest`
- `RegistroAccesoServiceTest`
- `SucursalServiceTest`
- `NotificacionServiceTest`

## Endpoint para defender

`POST /api/accesos/verificar`: recibe `miembroId`, consulta membresia activa y registra el resultado del acceso.

## Relacion de base de datos para explicar

En `access-service`, la entidad `RegistroAcceso` guarda cada intento de ingreso de un socio. La relacion logica principal es con el socio validado en `member-service` y la membresia activa validada en `membership-service`; por arquitectura de microservicios no se usa foreign key entre bases, sino identificadores (`miembroId`) y validacion remota. Esto mantiene independencia de base de datos y evita acoplar esquemas entre servicios.

En `notification-service`, la entidad `Notificacion` guarda mensajes asociados a un `miembroId`. La relacion con el socio tambien es logica/remota: antes de crear la notificacion se valida el miembro en `member-service`, manteniendo bases independientes por microservicio.

## Dificultad tecnica personal

La dificultad mas importante fue coordinar validaciones remotas entre servicios sin acoplar las bases de datos. Se resolvio manteniendo identificadores logicos (`miembroId`) y clientes REST para validar miembros, membresias y notificaciones antes de persistir operaciones propias de cada servicio.

## Checklist

- [x] Codigo del servicio incluido.
- [x] Pruebas unitarias incluidas.
- [x] Swagger disponible.
- [x] Dockerfile incluido.
- [x] Evidencia individual documentada para defensa.
