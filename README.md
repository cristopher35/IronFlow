# IronFlow — Sistema de Gestión de Gimnasio

Sistema de gestión integral para gimnasios basado en una **arquitectura de microservicios independientes**, desarrollado como Proyecto Semestral para la asignatura **DSY1103 — Desarrollo Fullstack I** del Instituto Profesional Duoc UC.

---

## 👥 Integrantes del Equipo — Grupo N°1

| Nombre | Microservicios desarrollados |
|---|---|
| **Cristopher Meneses** | member-service, membership-service, payment-service |
| **Hans Roman** | trainer-service, access-service, branch-service, notification-service |
| **Nicolás Vera** | class-service, booking-service, equipment-service |

---

## 📋 Descripción del Proyecto

**IronFlow** es una plataforma backend que aborda los principales problemas operativos del gimnasio: gestión de clientes, planes de membresía, pagos, reservas de clases, control de acceso, gestión de entrenadores, equipamiento y notificaciones.

La solución está compuesta por **10 microservicios independientes**, cada uno con su propia base de datos relacional, comunicándose entre sí mediante **API REST** con **RestTemplate**, siguiendo el patrón **CSR (Controller-Service-Repository)**.

---

## 🏗️ Arquitectura del Sistema

### Microservicios

| # | Microservicio | Puerto | Base de Datos | Responsabilidad |
|---|---|---|---|---|
| 1 | `member-service` | 8081 | db_members | Gestión de clientes del gimnasio |
| 2 | `membership-service` | 8082 | db_memberships | Planes de membresía y su asignación |
| 3 | `payment-service` | 8083 | db_payments | Procesamiento y registro de pagos |
| 4 | `class-service` | 8084 | db_classes | Catálogo de clases y programación |
| 5 | `booking-service` | 8085 | db_bookings | Reservas de clases |
| 6 | `trainer-service` | 8086 | db_trainers | Gestión de entrenadores |
| 7 | `access-service` | 8087 | db_access | Control de acceso al gimnasio |
| 8 | `equipment-service` | 8088 | db_equipment | Gestión de equipamiento |
| 9 | `branch-service` | 8089 | db_branches | Gestión de sucursales |
| 10 | `notification-service` | 8090 | db_notifications | Envío de notificaciones |

### Comunicación entre Microservicios

| Microservicio | Se comunica con | Propósito |
|---|---|---|
| `membership-service` | `member-service` | Validar que el miembro existe antes de asignar un plan |
| `payment-service` | `membership-service` | Validar plan activo antes de registrar pago |
| `class-service` | `trainer-service` | Validar que el entrenador existe al crear horario |
| `booking-service` | `class-service` + `member-service` | Validar miembro y horario antes de reservar |
| `access-service` | `membership-service` | Validar plan activo antes de permitir acceso |
| `notification-service` | `member-service` | Validar miembro antes de enviar notificación |

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 4.0.5 | Framework principal |
| **Spring Data JPA + Hibernate** | incluido | ORM y acceso a base de datos |
| **Spring Web MVC** | incluido | Exposición de endpoints REST |
| **RestTemplate** | incluido | Comunicación entre microservicios |
| **Lombok** | incluido | @Slf4j, @RequiredArgsConstructor, @Builder, @Data |
| **Jakarta Validation** | incluido | @NotBlank, @NotNull, @Positive, @Email, @Pattern |
| **H2 Database** | incluido | Base de datos en memoria (perfil h2) |
| **MySQL** | incluido | Base de datos local con XAMPP (perfil mysql) |
| **PostgreSQL** | incluido | Base de datos en la nube con Supabase (perfil supabase) |
| **Maven** | 3.x | Gestor de dependencias y build |
| **IntelliJ IDEA** | 2026.1 | IDE de desarrollo |

---

## 📦 Estructura de cada Microservicio

Cada microservicio sigue el patrón **CSR (Controller-Service-Repository)** con la siguiente estructura:

```
src/main/java/cl.duocuc.crmenesesn.<microservicio>/
├── client/           → Comunicación con otros microservicios (RestTemplate)
├── controller/       → Endpoints REST (@RestController)
├── dto/              → Records de Request y Response
├── exception/        → GlobalExceptionHandler (@RestControllerAdvice)
├── model/            → Entidades JPA (@Entity)
├── repository/       → Interfaces JpaRepository
└── service/          → Lógica de negocio (@Service + @Slf4j)
```

---

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos previos
- **Java 21** instalado y configurado en `JAVA_HOME`
- **Maven 3.x** (incluido en Maven Wrapper)
- **IntelliJ IDEA** o cualquier IDE compatible con Spring Boot

### Pasos para ejecutar un microservicio

1. Clona este repositorio:
```bash
git clone https://github.com/cristopher35/IronFlow.git
cd IronFlow
```

2. Entra a la carpeta de un microservicio (por ejemplo `member-service`):
```bash
cd member-service
```

3. Configura el JAVA_HOME (Windows PowerShell):
```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
```

4. Ejecuta el microservicio:
```bash
.\mvnw.cmd spring-boot:run
```

### Perfiles disponibles

Cada microservicio soporta 3 perfiles de base de datos:

```bash
# H2 en memoria (default para desarrollo rápido)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=h2

# MySQL local (requiere XAMPP corriendo)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql

# PostgreSQL con Supabase (requiere variables de entorno DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=supabase
```

---

## 🎯 Funcionalidades Implementadas

### Persistencia y Modelado
- ✅ Entidades JPA con `@Entity`, `@Id`, `@GeneratedValue`, `@ManyToOne`, `@OneToMany`
- ✅ Relaciones con integridad referencial entre entidades del mismo microservicio
- ✅ Cada microservicio tiene su propia base de datos independiente
- ✅ Configuración de datasource y dialecto para H2, MySQL y PostgreSQL

### Validaciones
- ✅ Bean Validation (Jakarta) en DTOs: `@NotBlank`, `@NotNull`, `@Positive`, `@Email`, `@Pattern`, `@Future`
- ✅ Validación de datos en controladores con `@Valid`
- ✅ Separación clara entre DTOs (Records) y entidades JPA

### Manejo de Excepciones
- ✅ `@RestControllerAdvice` con `GlobalExceptionHandler` en cada microservicio
- ✅ Códigos HTTP semánticos: 200, 201, 204, 400, 404, 409, 500
- ✅ Respuestas estructuradas y consistentes con `ResponseEntity`

### Logging Estructurado (SLF4J)
- ✅ `@Slf4j` de Lombok en todos los Services
- ✅ Niveles de log: `info`, `warn`, `error` según corresponda
- ✅ Trazabilidad completa entre capas (operaciones, errores, validaciones fallidas)

### Comunicación entre Microservicios
- ✅ Implementada con **RestTemplate** (nativo de Spring Boot)
- ✅ Manejo de excepciones con try/catch al llamar a servicios externos
- ✅ Validación de datos recibidos antes de procesar

### Reglas de Negocio
- ✅ RUT y email únicos en miembros
- ✅ Soft delete (estado INACTIVO) en lugar de eliminación física
- ✅ Un miembro no puede tener 2 planes ACTIVOS simultáneamente
- ✅ `fechaFin` del plan se calcula automáticamente
- ✅ No se puede anular un pago ya anulado
- ✅ No se puede crear horario para un tipo de clase INACTIVO
- ✅ No se puede eliminar un tipo de clase con horarios activos
- ✅ Validación de aforo en reservas

---

## 📐 Patrones de Diseño Implementados

| Patrón | Descripción |
|---|---|
| **CSR (Controller-Service-Repository)** | Separación de responsabilidades en 3 capas |
| **DTO Pattern** | Uso de Java Records (`XxxRequest`, `XxxResponse`) para no exponer entidades JPA |
| **Builder Pattern** | `@Builder` de Lombok para construcción de objetos |
| **Dependency Injection** | `@RequiredArgsConstructor` con inyección por constructor |
| **Global Exception Handling** | `@RestControllerAdvice` centralizando errores |
| **Soft Delete** | Cambio de estado en vez de eliminación física |

---

## 📚 Documentación Adicional

- **`IronFlow-Informe-Tecnico-Defensa.docx`** — Informe técnico completo del proyecto
- Documentos individuales de defensa por microservicio en formato `.docx`

---

## 📅 Información Académica

- **Asignatura:** DSY1103 — Desarrollo Fullstack I
- **Institución:** Instituto Profesional Duoc UC
- **Sede:** Escuela de Informática y Telecomunicaciones
- **Período:** 2026
- **Evaluación:** EP2 — Encargo con Defensa Técnica (45% del semestre)

---

## 📄 Licencia

Este proyecto es de uso académico y fue desarrollado para fines educativos en el contexto de la asignatura DSY1103 de Duoc UC.
