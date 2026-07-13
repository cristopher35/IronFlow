# Guia de despliegue Render

## Estado

Guia preparada para configurar los servicios en Render de forma manual. No se hizo despliegue en vivo desde esta maquina porque requiere cuenta Render, repositorio conectado y credenciales reales de base de datos Neon/Supabase. Las URL publicas y variables externas se completan cuando se creen los servicios en la cuenta del equipo.

Estado local previo al despliegue: Docker Compose fue validado end-to-end el 2026-07-12 con 12/12 contenedores `Up`, Gateway health 200, rutas por Eureka/Gateway 200 y seguridad 401/403.

## Servicios declarados

| Servicio Render | Ruta local | Tipo | Puerto |
|---|---|---|---|
| `ironflow-discovery-server` | `microservicios-ironflow/discovery-server` | Web Service | `8761` |
| `ironflow-api-gateway` | `microservicios-ironflow/api-gateway` | Web Service | `8080` |
| `ironflow-member-service` | `microservicios-ironflow/memberService/memberService/IronFlow` | Web Service | `8081` |
| `ironflow-membership-service` | `microservicios-ironflow/Membership-service` | Web Service | `8082` |
| `ironflow-payment-service` | `microservicios-ironflow/Payment-service` | Web Service | `8083` |
| `ironflow-class-service` | `microservicios-ironflow/class-service/class-service` | Web Service | `8084` |
| `ironflow-booking-service` | `microservicios-ironflow/booking-service/booking-service` | Web Service | `8085` |
| `ironflow-trainer-service` | `microservicios-ironflow/trainer-service/trainer-service` | Web Service | `8086` |
| `ironflow-access-service` | `microservicios-ironflow/access-service/access-service` | Web Service | `8087` |
| `ironflow-equipment-service` | `microservicios-ironflow/equipment-service/equipment-service/equipment-service` | Web Service | `8088` |
| `ironflow-branch-service` | `microservicios-ironflow/branch-service/branch-service` | Web Service | `8089` |
| `ironflow-notification-service` | `microservicios-ironflow/notification-service/notification-service` | Web Service | `8090` |

## Configuracion sugerida

Cada servicio debe usar:

- Build command: `./mvnw clean package -DskipTests`
- Start command: `java -jar target/*.jar`
- `SPRING_PROFILES_ACTIVE=neon` para servicios con perfil Neon y `supabase` para `class-service`, que no tiene perfil Neon.
- Variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` sin valores hardcodeados sensibles.
- `EUREKA_DEFAULT_ZONE` y URLs entre servicios como variables a completar con las URLs internas/publicas de Render.

## Pasos obligatorios para cerrar Render en vivo

1. Subir el proyecto final a GitHub.
2. En Render, crear los servicios web usando las rutas indicadas en esta guia.
3. Completar variables externas para cada servicio:
   - `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.
   - `EUREKA_DEFAULT_ZONE` apuntando al discovery server de Render.
   - URLs internas/publicas entre servicios (`MEMBER_SERVICE_URL`, `MEMBERSHIP_SERVICE_URL`, etc.).
4. Confirmar que los 12 servicios queden `Live`.
5. Probar:
   - `GET <gateway-url>/actuator/health` -> 200.
   - `GET <gateway-url>/member-app/api/members` con Basic Auth -> 200.
   - `GET <gateway-url>/class-app/api/schedules` con Basic Auth -> 200.
   - Caso sin credenciales -> 401.
   - Caso con rol insuficiente -> 403.
6. Reemplazar la tabla de URLs pendientes en este documento por las URLs publicas reales.
7. Adjuntar captura del dashboard Render y salida de los curls en la entrega.

## URLs a completar

| Servicio | URL Render |
|---|---|
| Discovery | Pendiente |
| Gateway | Pendiente |
| Swagger Gateway | Pendiente |
| Health Gateway | Pendiente |

## Validacion pendiente

Cuando se importe el blueprint, completar las variables de entorno, confirmar que `GET /actuator/health` quede en 200 para cada servicio y actualizar esta guia con las URLs finales. Hasta tener esas URLs publicas reales, Render queda documentado/preparado pero no validado en vivo.
