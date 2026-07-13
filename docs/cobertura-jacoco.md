# Cobertura Jacoco

Fecha de generacion local: 2026-07-12.

## Reportes incluidos y cobertura real

| Proyecto | Instrucciones | Ramas | Reporte HTML | CSV |
|---|---:|---:|---|---|
| access-service | 83.38% | 60.00% | `docs/cobertura-jacoco/access-service/index.html` | `docs/cobertura-jacoco/access-service/jacoco.csv` |
| api-gateway | 98.36% | 100.00% | `docs/cobertura-jacoco/api-gateway/index.html` | `docs/cobertura-jacoco/api-gateway/jacoco.csv` |
| booking-service | 87.10% | 80.00% | `docs/cobertura-jacoco/booking-service/index.html` | `docs/cobertura-jacoco/booking-service/jacoco.csv` |
| branch-service | 90.05% | 0.00% | `docs/cobertura-jacoco/branch-service/index.html` | `docs/cobertura-jacoco/branch-service/jacoco.csv` |
| class-service | 86.62% | 84.38% | `docs/cobertura-jacoco/class-service/index.html` | `docs/cobertura-jacoco/class-service/jacoco.csv` |
| discovery-server | 100.00% | 0.00% | `docs/cobertura-jacoco/discovery-server/index.html` | `docs/cobertura-jacoco/discovery-server/jacoco.csv` |
| equipment-service | 89.54% | 77.78% | `docs/cobertura-jacoco/equipment-service/index.html` | `docs/cobertura-jacoco/equipment-service/jacoco.csv` |
| member-service | 87.15% | 78.57% | `docs/cobertura-jacoco/member-service/index.html` | `docs/cobertura-jacoco/member-service/jacoco.csv` |
| membership-service | 88.16% | 70.00% | `docs/cobertura-jacoco/membership-service/index.html` | `docs/cobertura-jacoco/membership-service/jacoco.csv` |
| notification-service | 81.74% | 33.33% | `docs/cobertura-jacoco/notification-service/index.html` | `docs/cobertura-jacoco/notification-service/jacoco.csv` |
| payment-service | 87.20% | 50.00% | `docs/cobertura-jacoco/payment-service/index.html` | `docs/cobertura-jacoco/payment-service/jacoco.csv` |
| trainer-service | 88.78% | 62.50% | `docs/cobertura-jacoco/trainer-service/index.html` | `docs/cobertura-jacoco/trainer-service/jacoco.csv` |

## Comando usado

```bash
./mvnw org.jacoco:jacoco-maven-plugin:0.8.13:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.13:report -q
```

En `api-gateway` se puede usar su propio wrapper Maven:

```bash
cd microservicios-ironflow/api-gateway
./mvnw org.jacoco:jacoco-maven-plugin:0.8.13:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.13:report -q
```

Los 12 modulos quedan sobre 80% de cobertura de instrucciones. Para `discovery-server` se agrego un test de delegacion de `main` para cubrir el punto de entrada sin levantar Eureka de forma innecesaria.
