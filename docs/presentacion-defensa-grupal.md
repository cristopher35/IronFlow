# Presentacion defensa grupal - IronFlow

## Proyecto e integrantes

IronFlow - Sistema de Gestion de Gimnasio.

| Integrante | Microservicios |
|---|---|
| Cristopher Meneses | member-service, membership-service, payment-service, discovery-server/Eureka |
| Hans Roman | trainer-service, access-service, branch-service, notification-service |
| Nicolas Vera | class-service, booking-service, equipment-service, api-gateway |

## Problema abordado

El gimnasio opera con registros manuales para socios, pagos, clases y acceso. Esto genera perdida de trazabilidad, validaciones lentas, reservas informales y dificultad para revisar pagos o membresias activas.

## Solucion propuesta

Backend distribuido con 10 microservicios de negocio Spring Boot, persistencia JPA, DTOs, validaciones, Swagger/OpenAPI, pruebas unitarias, API Gateway y Eureka.

## Arquitectura

- Servicios de negocio: miembros, membresias, pagos, clases, reservas, entrenadores, accesos, equipos, sucursales y notificaciones.
- API Gateway en puerto 8080.
- Eureka/discovery-server en puerto 8761.
- Comunicacion HTTP con `RestTemplate` y `WebClient`.
- Bases de datos separadas por servicio.

## Flujo funcional principal

1. Registrar socio.
2. Crear plan.
3. Asignar plan activo.
4. Registrar pago.
5. Crear entrenador, clase y horario.
6. Reservar clase.
7. Validar acceso.
8. Registrar notificacion.

## Correcciones aplicadas

- Rutas del Gateway alineadas con rutas reales de servicios.
- Swagger estandarizado en `/doc/swagger-ui.html`.
- Maven wrappers habilitados localmente.
- Mockito estabilizado en membership-service y payment-service.
- Documentacion final y pruebas REST agregadas.

## Pruebas

- Pruebas unitarias por microservicio con JUnit 5 y Mockito.
- Archivo REST verificable: `docs/pruebas-rest/casos-prueba.http`.
- Swagger por servicio y por Gateway.

## Brechas reconocidas

- Render debe completarse con una cuenta del equipo para obtener URLs publicas.
- La autenticacion esta centralizada en gateway con HTTP Basic para la entrega local; JWT queda como mejora futura si el docente lo exige.

## Despliegue local

```bash
docker compose up --build
```

## URLs clave

- Gateway: `http://localhost:8080`
- Health: `http://localhost:8080/actuator/health`
- Swagger member-service: `http://localhost:8081/doc/swagger-ui.html`
- Swagger via Gateway: `http://localhost:8080/member-app/doc/swagger-ui.html`
