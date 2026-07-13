# Documentacion funcional - IronFlow

## Problema

IronFlow resuelve la gestion manual de un gimnasio: registro de socios, planes de membresia, pagos, reservas de clases, control de acceso, entrenadores, sucursales, equipamiento y notificaciones. El problema principal es evitar informacion dispersa, pagos no validados y reservas sin trazabilidad.

## Actores

| Actor | Necesidad |
|---|---|
| Cliente o socio | Registrarse, mantener datos actualizados, pagar membresia, reservar clases y recibir notificaciones. |
| Recepcionista | Verificar si un socio puede ingresar al gimnasio. |
| Administrador | Gestionar planes, pagos, sucursales, clases, horarios, entrenadores y equipamiento. |
| Entrenador | Estar asociado a horarios de clases. |

## Flujos principales

1. Alta de socio: `member-service` registra el socio y valida RUT/email unicos.
2. Alta de plan: `membership-service` crea planes comerciales.
3. Asignacion de plan: `membership-service` valida el socio en `member-service` y evita dos planes activos simultaneos.
4. Registro de pago: `payment-service` valida plan activo en `membership-service`.
5. Creacion de clase y horario: `class-service` registra disciplinas y horarios, validando entrenador remoto.
6. Reserva de clase: `booking-service` valida socio y horario antes de crear la reserva.
7. Control de acceso: `access-service` consulta membresia activa antes de registrar ingreso.
8. Notificacion: `notification-service` valida socio remoto antes de registrar una notificacion.

## Reglas de negocio implementadas

| Regla | Servicio | Evidencia |
|---|---|---|
| RUT y email de socio no se duplican. | `member-service` | `MiembroServiceTest`, `MiembroController` |
| Los socios se desactivan con baja logica. | `member-service` | `DELETE /api/members/{id}` |
| Un socio no puede tener dos planes activos. | `membership-service` | `PlanMiembroServiceTest` |
| Un pago no puede anularse dos veces. | `payment-service` | `PagoServiceTest` |
| Los horarios validan clase activa, fecha futura y entrenador. | `class-service` | `HorarioServiceImplTest` |
| Una reserva valida socio, horario y duplicidad activa. | `booking-service` | `ReservaServiceTest` |
| El acceso consulta membresia activa. | `access-service` | `RegistroAccesoServiceTest` |
| Equipamiento inactivo queda sin stock. | `equipment-service` | `EquipoServiceTest` |
| Notificaciones validan socio remoto. | `notification-service` | `NotificacionServiceTest` |

## Restricciones y alcance final

- El sistema expone 10 microservicios funcionales mas un API Gateway.
- La comunicacion entre servicios se realiza por HTTP usando RestClient, Feign Client y WebClient segun el servicio.
- El API Gateway usa rutas `lb://` contra Eureka/discovery server.
- El control de roles esta centralizado en el API Gateway con `ADMIN`, `SOCIO` y `RECEPCIONISTA`.
- El flujo de reservas valida miembro, horario, reserva duplicada y cupos reales de la clase; al cancelar libera el cupo.

## Datos de prueba sugeridos

Los ejemplos completos estan en `docs/pruebas-rest/casos-prueba.http`. Se recomienda ejecutar primero:

1. Crear socio.
2. Crear plan.
3. Asignar plan al socio.
4. Registrar pago.
5. Crear entrenador.
6. Crear tipo de clase.
7. Crear horario.
8. Crear reserva.
9. Verificar acceso.
10. Crear notificacion.
