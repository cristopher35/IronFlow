# Defensa Técnica Individual — Ecosistema Core, Monetización e Infraestructura
**Desarrollador:** Cristopher Meneses

---

## 1. Rol Arquitectónico y Dominio de Responsabilidad

Como responsable del backend core del sistema **IronFlow**, asumí el diseño, la implementación y la estrategia de desacoplamiento de los módulos críticos orientados a la **Gestión del Ciclo de Vida del Usuario (Socios), Monetización (Membresías y Pagos)** y la infraestructura base de **Orquestación y Descubrimiento de Servicios**. 

El ecosistema bajo mi dirección fue desarrollado bajo el enfoque arquitectónico de **Microservicios**, aislando los contextos delimitados mediante el patrón *Database-per-Service* para garantizar la escalabilidad horizontal y evitar puntos únicos de fallo (SPOF).

### Módulos e Infraestructura Asociada
* **`discovery-server`**: Infraestructura de orquestación basada en **Netflix Eureka Server**, actuando como el registro central dinámico del sistema.
* **`memberService`**: Microservicio encargado del gobierno de datos globales, identidad y credenciales de acceso de los socios.
* **`Membership-service`**: Motor de reglas de negocio para la gestión de contratos, catálogos de planes y control de vigencias de suscripción.
* **`Payment-service`**: Módulo transaccional aislado para la captura, procesamiento, conciliación y auditoría de los flujos de pago.

---

## 2. Funcionalidades de Alto Impacto y Reglas de Negocio

El desarrollo se centró en la automatización de flujos operacionales del gimnasio/plataforma, implementando validaciones estrictas en la capa de dominio:

* **Gobierno de Identidad e Integridad:** Implementación de flujos CRUD robustos para socios, incorporando filtros transaccionales que aseguran la estricta unicidad de identificadores críticos (**RUT** y **Email**) antes de la persistencia.
* **Motor de Suscripciones Dinámico:** Abstracción lógica para la definición mutábase de planes de membresía (costos, vigencias y restricciones).
* **Control de Invariantes de Negocio:** Algoritmo de asignación contractual que impide la superposición de suscripciones, forzando la regla de negocio de que un socio solo puede poseer **un único plan activo simultáneamente**.
* **Trazabilidad Financiera:** Orquestación síncrona para la emisión, validación cruzada y anulación de transacciones monetarias sin comprometer el rendimiento de los módulos restantes.

---

## 3. Patrones de Comunicación e Infraestructura Distributed

Para disolver el acoplamiento físico entre los servicios y flexibilizar el despliegue distribuido, se implementaron las siguientes soluciones:

* **Descubrimiento Dinámico de Servicios:** Configuración y aprovisionamiento de clientes Eureka (`@EnableDiscoveryClient`). Esto abstrae el direccionamiento IP y puertos de los servidores físicos, delegando la resolución de nombres de dominio de forma interna y transparente.
* **Comunicación Inter-servicio (Rest Client):** Integración de clientes HTTP remotos para la validación cruzada entre contextos de negocio (por ejemplo, cuando `membership-service` consulta la vigencia real de una entidad socio en `member-service`), manteniendo las fronteras lógicas y un bajo acoplamiento de bases de datos.

---

## 4. Estrategia de Calidad y Cobertura de Código (Testing)

La resiliencia de la lógica y la prevención de regresiones en el software se respaldaron con una suite automatizada de pruebas unitarias y de integración utilizando **JUnit 5** y **Mockito**, garantizando el aislamiento mediante dobles de prueba (*mocks*):

* **`MiembroServiceTest` & `MiembroControllerTest`**: Validación de contratos HTTP y aislamiento completo de las validaciones de unicidad de la capa de servicio.
* **`PlanServiceTest` & `PlanMiembroServiceTest`**: Pruebas sobre las invariantes complejas del negocio de membresías y el bloqueo lógico contra la doble suscripción.
* **`PagoServiceTest`**: Cobertura integral sobre el comportamiento de persistencia financiera, escenarios de error y flujos alternos de anulación.
* **Test de Carga en Infraestructura**: Pruebas de estrés controladas sobre el `discovery-server` para evaluar la tasa de respuesta en la resolución de nombres bajo ráfagas concurrentes de peticiones inter-servicio.

---

## 5. Endpoint Crítico Seleccionado para la Defensa

> **`POST /api/planes-miembros`**

### Justificación Técnica e Impacto
Este endpoint constituye el núcleo de la lógica distribuida del backend. Su ejecución requiere la orquestación segura de tres fases secuenciales de validación antes de confirmar la persistencia transaccional:
1. **Validación Remota de Entidad:** Comunicación síncrona orientada a verificar la existencia del socio y su estado legal dentro del dominio ajeno (`member-service`).
2. **Validación de Disponibilidad de Catálogo:** Consulta interna para certificar la vigencia técnica del plan seleccionado.
3. **Bloqueo Lógico de Invariante:** Evaluación del historial transaccional del socio para comprobar que **no posea un plan activo simultáneamente**, mitigando de raíz fugas de ingresos, dobles cobros o inconsistencias operacionales en los controles de acceso.

---

## 6. Modelo de Datos y Estrategia de Persistencia

Dentro del contexto de `membership-service`, el esquema relacional se estructuró específicamente para resguardar la inmutabilidad de los acuerdos contractuales históricos frente a modificaciones comerciales futuras.

```
       [ Plan ]
       (Catálogo)
          | 1
          |
          | N
   [ PlanMiembro ]  ----> (IdSocio referencial / FK Lógica externa)
    (Transaccional)
```

### Análisis Técnico de la Relación ($1:N$)
Un `Plan` (entidad maestra que define tarifa y periodicidad) se vincula a múltiples registros de `PlanMiembro`. Esta separación limpia permite cambiar el valor o los beneficios de un plan comercial en el mercado actual sin corromper ni alterar retroactivamente las condiciones y costos bajo los cuales un socio adquirió su membresía en el pasado en su respectivo registro transaccional.

La columna de estado (v.g., `estado_activo`) posee una restricción lógica indexada por código: para una clave compuesta implícita de `id_socio` + `estado = ACTIVO`, el conteo de registros en la base de datos debe ser estrictamente $\le 1$.

---

## 7. Complejidad Técnica Resuelta y Mitigación de Riesgos

La mayor dificultad del desarrollo consistió en sostener firmemente los principios de **diseño guiado por el dominio (DDD)** frente a la inercia del modelo monolítico tradicional. Al adoptar *Database-per-Service*, se eliminaron por completo los joins a nivel de base de datos entre socios, membresías y pagos.

### Estrategia de Mitigación
* **Aislamiento de Almacenamiento:** Cada microservicio gestiona su propia base de datos, comunicándose exclusivamente a través de APIs REST bien definidas, previniendo el acoplamiento de esquemas.
* **Alta Disponibilidad:** El registro dinámico en **Eureka Server** mitiga los fallos por cambios de infraestructura física, permitiendo balancear la carga e inicializar múltiples réplicas de los servicios de forma transparente.
* **Red de Seguridad:** Las reglas de negocio críticas se blindaron contra excepciones imprevistas mediante el uso riguroso de excepciones personalizadas, validadoras de datos de entrada y la suite de pruebas automatizadas como control de calidad previo al despliegue.

---

## 8. Matriz de Entregables e Indicadores Técnicos

* **[x] Código del Servicio Incluido:** Arquitectura modular basada en microservicios, implementada con separación limpia de responsabilidades (Controladores, Servicios, Repositorios).
* **[x] Pruebas Unitarias Incluidas:** Suite de pruebas automatizadas con cobertura en las capas críticas del backend.
* **[x] Swagger Disponible:** Documentación interactiva autogenerada bajo el estándar **OpenAPI / Swagger** para cada microservicio expuesto.
* **[x] Dockerfile Incluido:** Archivos de configuración docker optimizados para garantizar la portabilidad y la reproducibilidad en entornos de producción.
* **[x] Evidencia Individual Documentada:** Repositorio limpio con toda la trazabilidad de desarrollo requerida para la examinación técnica.
