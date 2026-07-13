# Defensa individual - Cristopher Meneses

## Rol

Responsable de los servicios de socios, membresias, pagos y discovery-server/Eureka.

## Modulos asociados

- `microservicios-ironflow/memberService/memberService/IronFlow`
- `microservicios-ironflow/Membership-service`
- `microservicios-ironflow/Payment-service`
- `microservicios-ironflow/discovery-server`

## Funcionalidades principales

- CRUD de socios.
- Validacion de RUT y email unicos.
- Creacion y actualizacion de planes.
- Asignacion de plan a socio activo.
- Registro y anulacion de pagos.
- Comunicacion de membership-service con member-service.
- Comunicacion de payment-service con membership-service.
- Registro y descubrimiento de servicios mediante Eureka.

## Pruebas asociadas

- `MiembroServiceTest`
- `MiembroControllerTest`
- `PlanServiceTest`
- `PlanMiembroServiceTest`
- `PagoServiceTest`
- Test de carga del `discovery-server`.

## Endpoint para defender

`POST /api/planes-miembros`: valida socio remoto, plan activo y evita doble plan activo.

## Relacion de base de datos para explicar

En `membership-service`, la relacion clave es `Plan` 1:N `PlanMiembro`. Un plan puede estar asociado a muchos socios mediante registros de `PlanMiembro`, pero cada asignacion apunta a un solo plan. Esta relacion permite mantener el catalogo de planes separado del historial/estado de membresias asignadas. La regla de negocio importante es que un socio no puede tener dos `PlanMiembro` activos simultaneamente.

## Dificultad tecnica personal

La parte mas delicada fue coordinar `member-service`, `membership-service`, `payment-service` y el registro en Eureka sin mezclar responsabilidades. La solucion fue dejar cada microservicio con su propia base, validar datos remotos por cliente REST, registrar los servicios en `discovery-server` y cubrir reglas criticas con pruebas unitarias: RUT/email unico, plan activo unico y pago/anulacion.

## Checklist

- [x] Codigo del servicio incluido.
- [x] Pruebas unitarias incluidas.
- [x] Swagger disponible.
- [x] Dockerfile incluido.
- [x] Evidencia individual documentada para defensa.
