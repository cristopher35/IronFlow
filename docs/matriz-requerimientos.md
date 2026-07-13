# Matriz de requerimientos - IronFlow

| ID | Requerimiento declarado | Tipo | Estado final | Endpoint/evidencia | Prueba asociada |
|---|---|---|---|---|---|
| RF-01 | Registrar nuevo cliente | Funcional | Implementado | `POST /api/members` | `MiembroServiceTest`, `MiembroControllerTest` |
| RF-02 | Consultar cliente | Funcional | Implementado | `GET /api/members/{id}` | `MiembroServiceTest` |
| RF-03 | Actualizar cliente | Funcional | Implementado | `PUT /api/members/{id}` | `MiembroServiceTest` |
| RF-04 | Desactivar cliente | Funcional | Implementado | `DELETE /api/members/{id}` | `MiembroServiceTest` |
| RF-05 | Crear plan de membresia | Funcional | Implementado | `POST /api/planes` | `PlanServiceTest` |
| RF-06 | Asignar membresia activa | Funcional | Implementado | `POST /api/planes-miembros` | `PlanMiembroServiceTest` |
| RF-07 | Registrar pago | Funcional | Implementado | `POST /api/payments` | `PagoServiceTest` |
| RF-08 | Crear clase y horario | Funcional | Implementado | `POST /api/classes`, `POST /api/schedules` | `HorarioServiceImplTest` |
| RF-09 | Consultar clases/horarios | Funcional | Implementado | `GET /api/classes`, `GET /api/schedules` | `HorarioServiceImplTest` |
| RF-10 | Reservar clase | Funcional | Implementado | `POST /api/bookings`, `PATCH /api/schedules/{id}/reservar` | `ReservaServiceTest`, `HorarioServiceImplTest` |
| RF-11 | Cancelar reserva | Funcional | Implementado | `DELETE /api/bookings/{id}` | `ReservaServiceTest` |
| RF-12 | Registrar acceso validando membresia | Funcional | Implementado | `POST /api/accesos/verificar` | `RegistroAccesoServiceTest` |
| RF-13 | Consultar historial de accesos | Funcional | Implementado | `GET /api/accesos/miembro/{miembroId}` | `RegistroAccesoServiceTest` |
| RF-14 | Registrar entrenadores | Funcional | Implementado | `POST /api/entrenadores` | `EntrenadorServiceTest` |
| RF-15 | Asignar entrenador a clase | Funcional | Implementado | `POST /api/schedules` | `HorarioServiceImplTest` |
| RF-16 | Gestionar equipamiento | Funcional agregado | Implementado | `POST /api/equipos`, `PUT /api/equipos/{id}` | `EquipoServiceTest` |
| RF-17 | Gestionar sucursales | Funcional agregado | Implementado | `POST /api/sucursales`, `GET /api/sucursales` | `SucursalServiceTest` |
| RF-18 | Registrar notificaciones | Funcional agregado | Implementado | `POST /api/notificaciones` | `NotificacionServiceTest` |
| RNF-01 | Respuestas HTTP coherentes | No funcional | Implementado | `ErrorApi(timestamp,status,error,message,path,fields)` en los 10 servicios | Pruebas unitarias y revision de handlers |
| RNF-02 | Base independiente por microservicio | No funcional | Implementado | `application*.properties`, `application.yml`, `docker-compose.yml` | Revision de configuracion |
| RNF-03 | Comunicacion API REST | No funcional | Implementado | RestClient en booking, Feign en payment, WebClient en otros clientes | Pruebas service |
| RNF-04 | Validacion de entrada | No funcional | Implementado | DTOs con Bean Validation y `@Valid` | Pruebas unitarias |
| RNF-05 | API Gateway | No funcional | Implementado | `microservicios-ironflow/api-gateway/src/main/resources/application.yml` | Gateway test |
| RNF-06 | Swagger/OpenAPI | No funcional | Implementado | `/doc/swagger-ui.html` por servicio | Revision manual |
| RNF-07 | Docker/Compose | No funcional | Validado localmente end-to-end con perfiles H2 en Compose | `Dockerfile`, `docker-compose.yml`; 12 contenedores `Up` | `docker compose config` OK; `COMPOSE_PARALLEL_LIMIT=1 docker compose up --build -d` OK; curls por Gateway 200/401/403 |
| RNF-08 | Eureka/discovery | No funcional | Implementado | `discovery-server`, Eureka clients, gateway con `lb://` | `discovery-server` y `api-gateway` tests |
| RNF-09 | Seguridad/roles | No funcional | Implementado en gateway | Spring Security Basic Auth con BCrypt y roles `ADMIN`, `SOCIO`, `RECEPCIONISTA` | `SecurityConfigTest` 401/403 |
| RNF-10 | Despliegue Render | No funcional | Preparado/documentado | `docs/render-despliegue.md` | Pendiente URL publica real |
| RNF-11 | Migraciones de base de datos por microservicio | No funcional | Implementado | `src/main/resources/db/migration/V1__create_schema.sql`, `V2__seed_data.sql` en los 10 servicios de negocio; perfiles persistentes con `ddl-auto=validate` | `./mvnw test` por servicio |
| RNF-12 | Cobertura unitaria minima | No funcional | Implementado | `docs/cobertura-jacoco.md`, `docs/cobertura-jacoco/*` | 12/12 modulos con JaCoCo instrucciones >= 80% |
