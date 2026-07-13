# Plan de cierre y feedback

| ID | Observacion o pendiente | Accion realizada | Archivos modificados/evidencia | Estado |
|---|---|---|---|---|
| FB-01 | Inconsistencias entre rutas directas y Gateway | Se removieron context paths internos y se agrego `StripPrefix=1` | `microservicios-ironflow/api-gateway/src/main/resources/application.yml`, `application*.properties` | Corregido |
| FB-02 | Swagger no uniforme | Se estandarizo Swagger en `/doc/swagger-ui.html` | `pom.xml`, controllers, config springdoc | Corregido |
| FB-03 | Maven wrappers sin permiso de ejecucion local | Se habilitaron wrappers locales | `mvnw` por servicio | Corregido |
| FB-04 | Tests de membership/payment fallaban por Mockito inline | Se agrego configuracion `mock-maker-subclass` | `src/test/resources/mockito-extensions` | Corregido |
| FB-05 | Faltaba evidencia documental final | Se agrego carpeta `docs` con matriz, plan, documentacion, pruebas REST y defensas base | `docs/**` | Corregido localmente |
| FB-06 | Faltaba `.env.example` sin secretos | Se agrego archivo de ejemplo | `.env.example` | Corregido |
| FB-07 | Zip por integrante no servia como entrega final completa | Se preparara zip general con raiz, docs, compose y servicios | `output/entrega-final/` | En validacion |
| FB-08 | Eureka/discovery requerido por rubrica | Implementado con `discovery-server`, clientes Eureka y gateway `lb://` | Tests OK | Cerrado local |
| FB-09 | Seguridad/roles requerido por rubrica | Implementado en `api-gateway` con BCrypt y roles `ADMIN`, `SOCIO`, `RECEPCIONISTA` | Tests 401/403 OK | Cerrado local |
| FB-10 | Migraciones Flyway requeridas por rubrica | Se agrego Flyway, DDL V1 y seed V2 en los 10 servicios de negocio; perfiles mysql/supabase/neon/dev pasan a `ddl-auto=validate` | `pom.xml`, `src/main/resources/db/migration/*.sql`, `application-*.yml/properties` | Cerrado local |
| FB-11 | Evidencia JaCoCo incompleta | Se agregaron pruebas JUnit/Mockito y se generaron/copiaron reportes para los 12 modulos; todos quedan con instrucciones >= 80% | `src/test/java/**`, `docs/cobertura-jacoco/**`, `docs/cobertura-jacoco.md` | Cerrado local |
| FB-14 | Validacion Docker Compose final | 2026-07-12: se valido Docker Desktop real (`Docker 29.6.1`, Compose `v5.2.0`), `docker compose config` sin warnings, `COMPOSE_PARALLEL_LIMIT=1 docker compose up --build -d`, 12/12 contenedores `Up`, Gateway health 200, rutas member/class/trainer 200 y seguridad 401/403 | `docker-compose.yml`, `docs/reporte-validacion-local.md`, `docs/matriz-requerimientos.md` | Cerrado local |
| FB-15 | Inconsistencia README sobre clientes REST | Se actualizo README y documentacion tecnica para declarar RestTemplate, WebClient, RestClient y Feign con servicio de origen/destino | `README.md`, `docs/documentacion-tecnica.md` | Cerrado local |
| FB-16 | Defensa individual incompleta | Se agregaron relaciones de base de datos, dificultad tecnica personal y evidencia/checklist actualizado por integrante | `docs/defensa-individual/*.md` | Cerrado local |
| FB-17 | Trazabilidad en Gateway | Se agrego filtro `X-Request-Id`, preservando el valor entrante o generando UUID, con pruebas automatizadas | `microservicios-ironflow/api-gateway/src/main/java/com/ironflow/gateway/config/RequestIdFilter.java`, `SecurityConfigTest` | Cerrado local |
| FB-18 | Limpieza de entrega final | Se excluyeron `output/`, `tmp/`, zips y artefactos locales en `.gitignore`; el ZIP final se genera sin carpetas de trabajo ni rutas locales fantasma | `.gitignore`, zip final | Cerrado local |
| FB-12 | Coleccion Postman requerida | Se tradujeron los casos existentes del `.http` a coleccion Postman v2.1 agrupada por servicio | `docs/pruebas-rest/coleccion-postman.json` | Cerrado local |
| FB-13 | Blueprint Render requerido | Se agrego `render.yaml` con 12 web services y variables a completar en Render | `render.yaml`, `docs/render-despliegue.md` | Cerrado local |

## Comandos de prueba unitaria

Ejecutar desde la raiz del repositorio:

```bash
(cd microservicios-ironflow/member-service && ./mvnw test)
(cd microservicios-ironflow/membership-service && ./mvnw test)
(cd microservicios-ironflow/payment-service && ./mvnw test)
(cd microservicios-ironflow/class-service && ./mvnw test)
(cd microservicios-ironflow/booking-service && ./mvnw test)
(cd microservicios-ironflow/trainer-service && ./mvnw test)
(cd microservicios-ironflow/access-service && ./mvnw test)
(cd microservicios-ironflow/equipment-service && ./mvnw test)
(cd microservicios-ironflow/branch-service && ./mvnw test)
(cd microservicios-ironflow/notification-service && ./mvnw test)
(cd microservicios-ironflow/api-gateway && ./mvnw test)
(cd microservicios-ironflow/discovery-server && ./mvnw test)
```

## Verificacion local recomendada

1. Ejecutar `docker compose up --build -d`.
2. Verificar `http://localhost:8080/actuator/health`.
3. Probar rutas Gateway con Basic Auth: `admin/admin123`, `socio/socio123`, `recepcion/recepcion123`.
4. Abrir Swagger directo y por Gateway.
5. Ejecutar `docs/pruebas-rest/casos-prueba.http`.
