# Documentacion tecnica - IronFlow

## Arquitectura general

IronFlow esta organizado como backend de microservicios Spring Boot con separacion Controller-Service-Repository, DTOs para entrada/salida, persistencia JPA y documentacion OpenAPI. Cada servicio tiene Maven Wrapper, `pom.xml`, configuracion propia y pruebas unitarias.

## Estructura del repositorio

| Ruta | Contenido |
|---|---|
| `memberService/memberService/IronFlow` | member-service |
| `MemberShip-service` | membership-service |
| `Payment-service` | payment-service |
| `class-service/class-service` | class-service |
| `booking-service/booking-service` | booking-service |
| `trainer-service/trainer-service` | trainer-service |
| `access-service/access-service` | access-service |
| `equipment-service/equipment-service/equipment-service` | equipment-service |
| `branch-service/branch-service` | branch-service |
| `notification-service/notification-service` | notification-service |
| `api-gateway` | API Gateway |
| `docs` | Documentacion, pruebas REST y defensas |
| `docker-compose.yml` | Orquestacion local/Docker |
| `.env.example` | Variables de entorno esperadas sin secretos |

## Responsabilidades por servicio

| Servicio | Puerto | Responsabilidad |
|---|---:|---|
| member-service | 8081 | Socios del gimnasio |
| membership-service | 8082 | Planes y asignacion de planes |
| payment-service | 8083 | Pagos y anulaciones |
| class-service | 8084 | Tipos de clase y horarios |
| booking-service | 8085 | Reservas y cancelaciones |
| trainer-service | 8086 | Entrenadores |
| access-service | 8087 | Verificacion de acceso |
| equipment-service | 8088 | Equipamiento |
| branch-service | 8089 | Sucursales |
| notification-service | 8090 | Notificaciones |
| api-gateway | 8080 | Punto de entrada y ruteo |

## Comunicacion entre servicios

| Origen | Destino | Cliente usado | Proposito |
|---|---|---|---|
| membership-service | member-service | RestTemplate | Validar socio activo |
| payment-service | membership-service | Feign | Validar plan activo |
| class-service | trainer-service | WebClient | Validar entrenador |
| booking-service | member-service | RestClient | Validar socio |
| booking-service | class-service | WebClient | Validar horario |
| access-service | membership-service | WebClient | Validar membresia para ingreso |
| notification-service | member-service | WebClient | Validar socio destinatario |

## API Gateway

El Gateway se ejecuta en `http://localhost:8080` y publica prefijos como `/member-app/**`, `/membership-app/**`, `/payment-app/**`, `/class-app/**`, `/booking-app/**`, `/trainer-app/**`, `/access-app/**`, `/equipment-app/**`, `/branch-app/**` y `/notification-app/**`. Cada ruta usa `StripPrefix=1`.

Para trazabilidad, el gateway agrega `X-IronFlow-Gateway` como header fijo y `X-Request-Id` por solicitud. Si el cliente envia `X-Request-Id`, se conserva; si no lo envia, el gateway genera un UUID.

## Perfiles y variables

- Servicios antiguos (`member`, `membership`, `payment`) tienen perfiles `dev`, `test` y `neon`.
- Servicios nuevos usan `application.yml` con H2 local o variables `DB_*` para perfil remoto.
- `.env.example` contiene las variables requeridas por `docker-compose.yml`.
- No se deben subir secretos reales.

## Ejecucion desde cero

Requisitos:

- Java 21.
- Maven Wrapper incluido por servicio.
- Docker para `docker compose up --build` cuando se quiera probar contenedores.

Pasos:

```bash
docker compose up --build -d
```

El `docker-compose.yml` de entrega usa perfil `h2` para levantar los 12 servicios localmente sin secretos. Los perfiles `neon`, `supabase` y `mysql` quedan disponibles por servicio para despliegue remoto o base persistente.

Para pruebas unitarias por servicio:

```bash
cd memberService/memberService/IronFlow && ./mvnw test
cd ../../../MemberShip-service && ./mvnw test
cd ../Payment-service && ./mvnw test
```

La lista completa de comandos esta en `docs/plan-cierre-feedback.md`.

## Swagger/OpenAPI

Cada servicio expone Swagger en `/doc/swagger-ui.html`. Por ejemplo:

- `http://localhost:8081/doc/swagger-ui.html`
- `http://localhost:8080/member-app/doc/swagger-ui.html`

## Seguridad

El `api-gateway` implementa Spring Security con autenticacion HTTP Basic y contrasenas codificadas con BCrypt en memoria para ejecucion local:

- `admin / admin123` con rol `ADMIN`.
- `socio / socio123` con rol `SOCIO`.
- `recepcion / recepcion123` con rol `RECEPCIONISTA`.

Las reglas de autorizacion se aplican por ruta del gateway. Por ejemplo, sucursales y equipamiento requieren `ADMIN`, mientras reservas y clases aceptan `ADMIN`, `RECEPCIONISTA` o `SOCIO`. La prueba `SecurityConfigTest` valida 401 sin credenciales y 403 con rol insuficiente.

Decision de diseno: los usuarios estan en `InMemoryUserDetailsManager` para que la defensa local y Compose funcionen sin un servicio adicional de identidad. Para produccion, la mejora natural es persistir usuarios/roles en una base propia o delegar autenticacion a un proveedor externo.

## Discovery

El proyecto incluye `discovery-server` con Eureka Server en el puerto `8761`. Los 10 microservicios y el `api-gateway` se registran como Eureka clients usando `EUREKA_DEFAULT_ZONE`. El gateway usa rutas `lb://nombre-servicio` para balanceo por nombre logico.

## Pruebas

Hay pruebas unitarias con JUnit/Mockito en todos los microservicios de negocio. Las pruebas REST estan documentadas en `docs/pruebas-rest/casos-prueba.http`.

## Despliegue Render

El proyecto incluye `render.yaml` y `docs/render-despliegue.md` como blueprint de despliegue para los 12 servicios. El despliegue publico no se ejecuto desde esta maquina porque requiere cuenta Render, conexion del repositorio y credenciales externas de base de datos. No se declara como validado en vivo mientras no existan URLs publicas reales.
