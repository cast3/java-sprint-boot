# 🚀 Product Service — Microservicio con Spring Boot y Arquitectura Limpia

> Proyecto de aprendizaje progresivo para practicar Java + Spring Boot con una estructura
> profesional, orientado a desarrolladores junior que quieren escalar hacia el nivel senior.

---

## 📋 Tabla de Contenidos

- [¿Qué aprenderás?](#-qué-aprenderás)
- [Tecnologías y librerías](#-tecnologías-y-librerías)
- [Arquitectura del proyecto](#-arquitectura-del-proyecto)
- [Estructura de carpetas](#-estructura-de-carpetas)
- [Cómo ejecutar el proyecto](#-cómo-ejecutar-el-proyecto)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Documentación automática (Swagger)](#-documentación-automática-swagger)
- [Ejecutar con Docker](#-ejecutar-con-docker)
- [Ejecutar los tests](#-ejecutar-los-tests)
- [Ejercicios para practicar](#-ejercicios-para-practicar)
- [Próximos pasos (escalando el proyecto)](#-próximos-pasos-escalando-el-proyecto)

---

## 🎯 ¿Qué aprenderás?

| Concepto | Descripción |
|---|---|
| **Arquitectura Limpia** | Separar el código en capas independientes: dominio, aplicación e infraestructura |
| **Hexagonal / Ports & Adapters** | Aislar el núcleo del negocio de los detalles técnicos (base de datos, HTTP) |
| **Spring Boot 3.x** | Framework para construir aplicaciones Java de forma rápida y productiva |
| **REST API** | Diseño de endpoints con buenas prácticas HTTP |
| **Validaciones** | Validar la entrada del usuario antes de procesar los datos |
| **Manejo de errores** | Respuestas de error consistentes y descriptivas |
| **ORM / JPA** | Mapear objetos Java a tablas de base de datos |
| **Docker** | Contenerizar la aplicación y su base de datos |
| **Lombok** | Eliminar código repetitivo (getters, setters, constructores) |
| **MapStruct** | Mapear objetos entre capas sin código manual |
| **Swagger/OpenAPI** | Documentar y probar la API automáticamente |
| **Testing** | Tests unitarios con JUnit 5 y Mockito |

---

## 🛠️ Tecnologías y librerías

| Herramienta | Versión | ¿Para qué sirve? |
|---|---|---|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.2.x | Framework principal |
| Spring Web | - | Crear endpoints REST |
| Spring Data JPA | - | Acceso a base de datos con ORM |
| Spring Validation | - | Validar entradas del usuario |
| Spring Actuator | - | Monitoreo y health checks |
| Lombok | 1.18.x | Reducir código boilerplate |
| MapStruct | 1.5.x | Mapeo entre capas |
| SpringDoc OpenAPI | 2.3.x | Documentación automática (Swagger UI) |
| H2 Database | - | Base de datos en memoria (desarrollo) |
| PostgreSQL | 16 | Base de datos relacional (producción) |
| Docker | - | Contenerización |
| JUnit 5 + Mockito | - | Testing |

---

## 🏛️ Arquitectura del proyecto

Este proyecto implementa **Arquitectura Limpia** combinada con el patrón **Hexagonal (Ports & Adapters)**:

```
┌─────────────────────────────────────────────────────────────┐
│                      INFRAESTRUCTURA                         │
│                                                             │
│   ┌──────────────┐         ┌──────────────────────────┐    │
│   │  REST API    │         │   Adaptador Persistencia  │    │
│   │ (Controller) │         │  (JPA / PostgreSQL / H2)  │    │
│   └──────┬───────┘         └───────────────┬───────────┘    │
│          │ Puerto de entrada                │ Puerto de salida│
│   ┌──────▼──────────────────────────────────▼───────┐       │
│   │                  APLICACIÓN                      │       │
│   │           ProductService (casos de uso)          │       │
│   └──────────────────────┬───────────────────────────┘       │
│                          │                                   │
│   ┌──────────────────────▼───────────────────────────┐       │
│   │                   DOMINIO                         │       │
│   │   Product (modelo) + Interfaces (puertos)         │       │
│   └───────────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
```

### Regla de dependencia
> Las capas internas **nunca** conocen las capas externas.
> El dominio no importa Spring, JPA, ni ningún framework.

---

## 📁 Estructura de carpetas

```
src/main/java/com/example/productservice/
│
├── ProductServiceApplication.java          ← Punto de entrada
│
├── domain/                                  ← Capa de dominio (núcleo del negocio)
│   ├── model/
│   │   └── Product.java                    ← Entidad de dominio
│   ├── port/
│   │   ├── in/
│   │   │   └── ProductUseCase.java         ← Puerto de entrada (qué puede hacer el sistema)
│   │   └── out/
│   │       └── ProductRepositoryPort.java  ← Puerto de salida (cómo guardar datos)
│   └── exception/
│       └── ProductNotFoundException.java   ← Excepción de dominio
│
├── application/                             ← Capa de aplicación (casos de uso)
│   └── service/
│       └── ProductService.java             ← Implementación de los casos de uso
│
└── infrastructure/                          ← Capa de infraestructura (detalles técnicos)
    ├── adapter/
    │   ├── in/
    │   │   └── web/                        ← Adaptador REST
    │   │       ├── ProductController.java
    │   │       ├── GlobalExceptionHandler.java
    │   │       └── dto/
    │   │           ├── ProductRequest.java
    │   │           ├── ProductResponse.java
    │   │           └── ProductMapper.java
    │   └── out/
    │       └── persistence/                ← Adaptador de base de datos
    │           ├── ProductEntity.java
    │           ├── ProductJpaRepository.java
    │           ├── ProductPersistenceAdapter.java
    │           └── ProductEntityMapper.java
    └── config/
        └── BeanConfiguration.java          ← Configuración e inyección de dependencias
```

---

## ▶️ Cómo ejecutar el proyecto

### Requisitos previos
- Java 17+
- Maven 3.8+ (o usar el wrapper `./mvnw`)
- Docker Desktop (opcional, para PostgreSQL)

### Modo desarrollo (H2 en memoria)

```bash
# 1. Clonar el repositorio
git clone https://github.com/cast3/java-sprint-boot.git
cd java-sprint-boot

# 2. Compilar el proyecto
./mvnw clean package -DskipTests

# 3. Ejecutar la aplicación (perfil dev, usa H2)
./mvnw spring-boot:run

# La aplicación estará disponible en:
# http://localhost:8080
# Consola H2: http://localhost:8080/h2-console
```

---

## 🌐 Endpoints de la API

Base URL: `http://localhost:8080/api/v1`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/products` | Listar todos los productos |
| `GET` | `/products/{id}` | Obtener un producto por ID |
| `POST` | `/products` | Crear un nuevo producto |
| `PUT` | `/products/{id}` | Actualizar un producto |
| `DELETE` | `/products/{id}` | Eliminar un producto |

### Ejemplo: Crear un producto

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Gaming",
    "description": "Laptop de alto rendimiento para gaming",
    "price": 1299.99,
    "stock": 50
  }'
```

### Respuesta esperada (201 Created)

```json
{
  "id": 1,
  "name": "Laptop Gaming",
  "description": "Laptop de alto rendimiento para gaming",
  "price": 1299.99,
  "stock": 50,
  "available": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

## 📖 Documentación automática (Swagger)

Con la aplicación corriendo, abre en tu navegador:

```
http://localhost:8080/swagger-ui.html
```

Ahí podrás ver y probar todos los endpoints sin necesidad de Postman o curl.

---

## 🐳 Ejecutar con Docker

```bash
# Construir imagen y levantar todos los servicios (app + PostgreSQL)
docker-compose up --build

# Ver los logs
docker-compose logs -f product-service

# Detener todo
docker-compose down

# Detener y eliminar datos de PostgreSQL
docker-compose down -v
```

### Variables de entorno disponibles

| Variable | Default | Descripción |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` | Perfil de Spring (dev/prod) |
| `DB_HOST` | `localhost` | Host de PostgreSQL |
| `DB_PORT` | `5432` | Puerto de PostgreSQL |
| `DB_NAME` | `productdb` | Nombre de la base de datos |
| `DB_USER` | `postgres` | Usuario de PostgreSQL |
| `DB_PASSWORD` | `postgres` | Contraseña de PostgreSQL |

---

## 🧪 Ejecutar los tests

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar un test específico
./mvnw test -Dtest=ProductServiceTest

# Ver reporte de cobertura
./mvnw verify
```

---

## 💪 Ejercicios para practicar

> Completa estos ejercicios en orden para ir ganando confianza.

### Nivel Principiante 🟢
1. **Agregar campo `category`** al producto (String). Actualiza el modelo, la entidad JPA, los DTOs y el mapper.
2. **Filtrar por disponibilidad**: Agrega un endpoint `GET /products?available=true` que solo devuelva productos en stock.
3. **Búsqueda por nombre**: Agrega `GET /products/search?name=laptop` para buscar productos por nombre (pista: usa `findByNameContainingIgnoreCase` en `ProductJpaRepository`).

### Nivel Intermedio 🟡
4. **Paginación**: Modifica `GET /products` para soportar paginación (`?page=0&size=10`). Usa `Page<ProductEntity>` y `Pageable`.
5. **Nuevo microservicio**: Crea un `CategoryService` con su propio CRUD, siguiendo la misma estructura de carpetas.
6. **Validación personalizada**: Crea una anotación `@ValidPrice` que valide que el precio tenga máximo 2 decimales.

### Nivel Avanzado 🔴
7. **Spring Security**: Agrega autenticación JWT. Solo usuarios autenticados pueden crear/editar/borrar productos.
8. **Caché con Redis**: Agrega caché en `findAll()` y `findById()` usando `@Cacheable` con Redis.
9. **Mensajería con RabbitMQ**: Publica un evento `ProductCreatedEvent` cuando se crea un producto, y crea un segundo microservicio que lo consuma.
10. **Observabilidad**: Integra Prometheus + Grafana para visualizar métricas del microservicio.

---

## 📈 Próximos pasos (escalando el proyecto)

Una vez que domines este microservicio, el siguiente nivel incluye:

```
┌─────────────────────────────────────────────────────────┐
│                  ECOSISTEMA DE MICROSERVICIOS            │
│                                                         │
│  [API Gateway]  →  [Product Service]                   │
│       │         →  [Order Service]                     │
│       │         →  [User Service]                      │
│                                                         │
│  [Service Discovery - Eureka]                          │
│  [Config Server]                                       │
│  [Message Broker - RabbitMQ / Kafka]                  │
│  [Distributed Tracing - Zipkin]                       │
└─────────────────────────────────────────────────────────┘
```

| Tecnología | ¿Para qué? |
|---|---|
| Spring Cloud Gateway | API Gateway para enrutar peticiones |
| Netflix Eureka | Descubrimiento de servicios |
| Spring Cloud Config | Configuración centralizada |
| RabbitMQ / Kafka | Comunicación asíncrona entre servicios |
| Zipkin / Jaeger | Trazabilidad distribuida |
| Redis | Caché distribuida |
| Kubernetes | Orquestación de contenedores |

---

## 🩺 Health Check

```bash
curl http://localhost:8080/actuator/health
```

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

---

## 📚 Recursos recomendados

- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Clean Architecture — Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Baeldung — Spring Boot Tutorials](https://www.baeldung.com/spring-boot)

---

> 💡 **Tip para juniors**: No intentes entender todo de golpe. Empieza leyendo el `ProductController`,
> sigue hacia `ProductService` y termina en `ProductPersistenceAdapter`.
> Así verás el flujo completo de una petición HTTP hasta la base de datos.
