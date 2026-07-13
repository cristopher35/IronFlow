# Reporte de validacion local

Fecha de validacion local: 2026-07-12.
Revalidacion final de entrega: 2026-07-13.

## Resultado general

| Validacion | Resultado |
|---|---|
| `docker-compose.yml` parsea como YAML valido | OK con `docker compose config` |
| Tests unitarios de los 10 microservicios | OK |
| Test de discovery-server | OK |
| Tests del API Gateway, incluyendo 401/403 por roles | OK |
| Trazabilidad `X-Request-Id` en API Gateway | OK: preserva header entrante y genera uno si falta |
| Reportes Jacoco copiados a `docs/cobertura-jacoco/` | OK, 12 modulos con reporte |
| Cobertura JaCoCo minima por modulo | OK, 12/12 modulos sobre 80% de instrucciones |
| Docker Compose ejecutado localmente | OK: `docker compose up --build -d` construyo y levanto 12/12 contenedores |
| Health `GET http://localhost:8080/actuator/health` | OK: HTTP 200, `{"groups":["liveness","readiness"],"status":"UP"}` |
| Revalidacion final 2026-07-13 | OK: 12/12 contenedores `Up`, Gateway health 200, rutas 200 y seguridad 401/403 |

## Servicios probados

| Servicio | Comando | Resultado |
|---|---|---|
| member-service | `./mvnw test` | OK |
| membership-service | `./mvnw test` | OK |
| payment-service | `./mvnw test` | OK |
| class-service | `./mvnw test` | OK |
| booking-service | `./mvnw test` | OK |
| trainer-service | `./mvnw test` | OK |
| access-service | `./mvnw test` | OK |
| equipment-service | `./mvnw test` | OK |
| branch-service | `./mvnw test` | OK |
| notification-service | `./mvnw test` | OK |
| api-gateway | `./mvnw test` | OK |
| discovery-server | `./mvnw test` | OK |

## Cobertura Jacoco

Se generaron reportes Jacoco para los 12 modulos: discovery-server, api-gateway y los 10 microservicios de negocio. Todos quedaron sobre 80% de instrucciones. La evidencia esta en `docs/cobertura-jacoco.md` y en las carpetas `docs/cobertura-jacoco/*`.

Comando base usado:

```bash
./mvnw org.jacoco:jacoco-maven-plugin:0.8.13:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.13:report -q
```

## Docker Compose

Validacion ejecutada desde la raiz el 2026-07-12.

```bash
docker --version
docker compose version
docker compose config
COMPOSE_PARALLEL_LIMIT=1 docker compose up --build -d
docker compose ps
curl -i http://localhost:8080/actuator/health
curl -i -u admin:admin123 http://localhost:8080/member-app/api/members
curl -i -u socio:socio123 http://localhost:8080/class-app/api/schedules
curl -i -u admin:admin123 http://localhost:8080/trainer-app/api/entrenadores
curl -i http://localhost:8080/member-app/api/members
curl -i -u socio:socio123 http://localhost:8080/trainer-app/api/entrenadores
cd microservicios-ironflow/api-gateway && ./mvnw test
docker compose up --build -d api-gateway
curl -i -H 'X-Request-Id: defensa-ironflow-final' http://localhost:8080/actuator/health
curl -i http://localhost:8080/actuator/health
```

Resultado real observado:

```text
Docker version 29.6.1, build 8900f1d
Docker Compose version v5.2.0
docker compose config: OK, sin warnings despues de dejar perfiles locales H2 en docker-compose.yml.
docker compose up --build -d: OK. Se construyeron las 12 imagenes y se crearon/arrancaron los 12 contenedores.
```

Estado de `docker compose ps`:

```text
access-service         Up About a minute   0.0.0.0:8087->8087/tcp
api-gateway            Up About a minute   0.0.0.0:8080->8080/tcp
booking-service        Up About a minute   0.0.0.0:8085->8085/tcp
branch-service         Up About a minute   0.0.0.0:8089->8089/tcp
class-service          Up About a minute   0.0.0.0:8084->8084/tcp
discovery-server       Up About a minute   0.0.0.0:8761->8761/tcp
equipment-service      Up About a minute   0.0.0.0:8088->8088/tcp
member-service         Up About a minute   0.0.0.0:8081->8081/tcp
membership-service     Up About a minute   0.0.0.0:8082->8082/tcp
notification-service   Up About a minute   0.0.0.0:8090->8090/tcp
payment-service        Up About a minute   0.0.0.0:8083->8083/tcp
trainer-service        Up About a minute   0.0.0.0:8086->8086/tcp
```

Verificacion HTTP por Gateway:

```text
GET /actuator/health -> HTTP 200, {"groups":["liveness","readiness"],"status":"UP"}
GET /member-app/api/members con admin:admin123 -> HTTP 200, []
GET /class-app/api/schedules con socio:socio123 -> HTTP 200, []
GET /trainer-app/api/entrenadores con admin:admin123 -> HTTP 200, []
GET /member-app/api/members sin credenciales -> HTTP 401
GET /trainer-app/api/entrenadores con socio:socio123 -> HTTP 403
GET /actuator/health con X-Request-Id=defensa-ironflow-final -> HTTP 200, response header X-Request-Id: defensa-ironflow-final
GET /actuator/health sin X-Request-Id -> HTTP 200, response header X-Request-Id generado como UUID
```

Pruebas adicionales de Gateway tras agregar trazabilidad:

```text
api-gateway ./mvnw test -> BUILD SUCCESS
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Nota: Compose no define `healthcheck`, por eso el estado real aparece como `Up` en vez de `healthy`.

## Observaciones

- Los warnings de Mockito sobre self-attach no detuvieron la ejecucion.
- Docker Desktop quedo instalado localmente; la validacion completa con Compose queda OK.
- Para ejecucion local se dejaron perfiles H2 en `docker-compose.yml`, evitando credenciales externas y permitiendo levantar la arquitectura completa.
- Eureka/discovery esta implementado con `discovery-server` y clientes Eureka en los 10 servicios mas gateway.
- Spring Security esta implementado en `api-gateway` con usuarios locales BCrypt y roles `ADMIN`, `SOCIO`, `RECEPCIONISTA`. Las pruebas cubren 401 sin credenciales y 403 con rol insuficiente.
- El API Gateway agrega trazabilidad HTTP con `X-Request-Id`.
- El despliegue Render no fue ejecutado desde este equipo; queda documentado en `docs/render-despliegue.md` para completarlo con una cuenta Render.
- Se agregaron migraciones Flyway en los 10 servicios de negocio y los perfiles persistentes quedaron con `ddl-auto=validate`.

## Revalidacion final de entrega 2026-07-13

Comandos ejecutados desde la raiz del repositorio:

```bash
docker compose config
docker compose ps
curl -i http://localhost:8080/actuator/health
curl -i -u admin:admin123 http://localhost:8080/member-app/api/members
curl -i -u socio:socio123 http://localhost:8080/class-app/api/schedules
curl -i -u admin:admin123 http://localhost:8080/trainer-app/api/entrenadores
curl -i http://localhost:8080/member-app/api/members
curl -i -u socio:socio123 http://localhost:8080/trainer-app/api/entrenadores
```

Resultado observado:

```text
docker compose config -> OK
docker compose ps -> 12/12 contenedores Up
GET /actuator/health -> HTTP 200, {"groups":["liveness","readiness"],"status":"UP"}
GET /member-app/api/members con admin:admin123 -> HTTP 200, []
GET /class-app/api/schedules con socio:socio123 -> HTTP 200, []
GET /trainer-app/api/entrenadores con admin:admin123 -> HTTP 200, []
GET /member-app/api/members sin credenciales -> HTTP 401
GET /trainer-app/api/entrenadores con socio:socio123 -> HTTP 403
```
