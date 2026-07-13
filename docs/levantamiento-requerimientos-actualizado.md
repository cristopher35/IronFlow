# Levantamiento de requerimientos actualizado

Este documento contrasta el levantamiento inicial de IronFlow con el estado final del repositorio local.

| ID | Requerimiento original | Cambio realizado | Justificacion | Estado final | Evidencia |
|---|---|---|---|---|---|
| RF-01 a RF-04 | Gestion de clientes | Se mantuvo | Es el nucleo de socios | Implementado | `member-service` |
| RF-05 a RF-06 | Planes y membresias | Se mantuvo y se agrego validacion remota de socio | Evita planes activos duplicados | Implementado | `membership-service` |
| RF-07 | Pagos | Se mantuvo y se agrego anulacion | Permite trazabilidad de pagos | Implementado | `payment-service` |
| RF-08 a RF-09 | Clases y horarios | Se mantuvo | Permite programar actividades | Implementado | `class-service` |
| RF-10 | Reserva con control de cupo | Se ajusto | El sistema valida miembro, horario y reserva duplicada; el decremento transaccional de cupos queda pendiente | Implementado parcialmente | `booking-service` |
| RF-11 | Cancelar reserva | Se mantuvo | Libera logisticamente la reserva | Implementado | `booking-service` |
| RF-12 a RF-13 | Control de acceso | Se mantuvo | Valida membresia antes del ingreso | Implementado | `access-service` |
| RF-14 a RF-15 | Entrenadores | Se mantuvo | Necesario para horarios | Implementado | `trainer-service`, `class-service` |
| RF-16 | Equipamiento | Se agrego | Amplia la gestion operativa del gimnasio | Implementado | `equipment-service` |
| RF-17 | Sucursales | Se agrego | Permite administrar sedes | Implementado | `branch-service` |
| RF-18 | Notificaciones | Se agrego | Registra avisos relacionados a socios | Implementado | `notification-service` |
| RNF Gateway | Punto de entrada unico | Se agrego API Gateway | Centraliza rutas publicas | Implementado | `api-gateway` |
| RNF Swagger | Documentacion de API | Se estandarizo ruta `/doc/swagger-ui.html` | Facilita defensa y pruebas | Implementado | Controllers + springdoc |
| RNF Docker | Ejecucion reproducible | Se agregaron Dockerfiles y compose | Permite levantar servicios por contenedor | Implementado con variables externas | `docker-compose.yml` |
| RNF Discovery | Eureka/discovery | Implementado local | `discovery-server`, clientes Eureka y gateway con `lb://` | Cerrado local | Validado con tests |
| RNF Seguridad | Roles/autenticacion | Implementado local | Spring Security en gateway con BCrypt y roles | Cerrado local | Tests 401/403 |
