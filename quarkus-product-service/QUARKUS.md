# ⚡ Quarkus con Java — Guía de Aprendizaje

> Microservicio de productos con **Quarkus 3.x** y **Arquitectura Limpia**.
> Compara cada concepto lado a lado con la versión Spring Boot del proyecto.

---

## 📋 Tabla de Contenidos

- [¿Qué es Quarkus?](#-qué-es-quarkus)
- [Quarkus vs Spring Boot](#-quarkus-vs-spring-boot)
- [Tecnologías usadas](#-tecnologías-usadas)
- [Arquitectura del módulo](#-arquitectura-del-módulo)
- [Estructura de carpetas](#-estructura-de-carpetas)
- [Cómo ejecutar](#-cómo-ejecutar)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Diferencias clave: Spring Boot vs Quarkus](#-diferencias-clave-spring-boot-vs-quarkus)
- [Conceptos importantes de Quarkus](#-conceptos-importantes-de-quarkus)
- [Ejecutar con Docker](#-ejecutar-con-docker)
- [Ejecutar los tests](#-ejecutar-los-tests)
- [Ejercicios para practicar](#-ejercicios-para-practicar)
- [Modo Nativo (GraalVM)](#-modo-nativo-graalvm)

---

## 🤔 ¿Qué es Quarkus?

**Quarkus** es un framework Java de nueva generación diseñado para la era cloud-native y microservicios. Sus características principales:

| Característica | Descripción |
|---|---|
| **Startup ultrarrápido** | Inicia en milisegundos (ideal para serverless y Kubernetes) |
| **Huella de memoria pequeña** | Consume mucha menos RAM que Spring Boot |
| **Build time processing** | Mueve el trabajo del arranque al tiempo de compilación |
| **Compilación nativa** | Con GraalVM puede compilar a binario nativo sin JVM |
| **Standards-based** | Usa estándares de Jakarta EE (CDI, JAX-RS) y MicroProfile |
| **Dev Mode** | Live reload automático como si fuera JavaScript |

---

## ⚔️ Quarkus vs Spring Boot

```
┌─────────────────────┬──────────────────────┬──────────────────────┐
│ Aspecto             │ Spring Boot           │ Quarkus              │
├─────────────────────┼──────────────────────┼──────────────────────┤
│ Startup time        │ ~2-5 segundos         │ ~0.1-0.5 segundos    │
│ Memoria en reposo   │ ~200-400 MB           │ ~50-150 MB           │
│ API REST            │ Spring MVC / WebFlux  │ RESTEasy (JAX-RS)    │
│ Inyección Dep.      │ Spring IoC (@Bean)    │ CDI (@Inject)        │
│ Persistencia        │ Spring Data JPA       │ Hibernate + Panache  │
│ Validación          │ Spring Validation     │ Hibernate Validator  │
│ OpenAPI             │ SpringDoc             │ SmallRye OpenAPI     │
│ Health checks       │ Spring Actuator       │ SmallRye Health      │
│ Testing             │ @SpringBootTest       │ @QuarkusTest         │
│ Logging             │ SLF4J / Logback       │ JBoss Logging        │
│ Config              │ application.yml       │ application.properties│
│ Profiles            │ spring.profiles       │ %dev., %prod.        │
│ Modo nativo         │ Spring Native (GraalVM│ Nativo (GraalVM)     │
└─────────────────────┴──────────────────────┴──────────────────────┘
```

### ¿Cuándo usar cada uno?

**Usa Spring Boot cuando:**
- El equipo ya conoce Spring
- Necesitas ecosistema maduro con muchas integraciones
- El proyecto es una aplicación tradicional (no serverless)

**Usa Quarkus cuando:**
- Despliegas en Kubernetes / serverless
- El tiempo de inicio y la memoria son críticos
- Quieres usar estándares de Jakarta EE / MicroProfile
- Planeas compilar a binario nativo

---

## 🛠️ Tecnologías usadas

| Librería Quarkus | Equivalente Spring Boot | ¿Para qué? |
|---|---|---|
| `quarkus-resteasy-reactive-jackson` | `spring-boot-starter-web` | API REST con JSON |
| `quarkus-hibernate-orm-panache` | `spring-boot-starter-data-jpa` | ORM con acceso simplificado a BD |
| `quarkus-hibernate-validator` | `spring-boot-starter-validation` | Validación Bean Validation |
| `quarkus-smallrye-openapi` | `springdoc-openapi-starter-webmvc-ui` | Swagger UI / OpenAPI |
| `quarkus-smallrye-health` | `spring-boot-starter-actuator` | Health checks |
| `quarkus-jdbc-h2` | `h2` | BD en memoria (dev/test) |
| `quarkus-jdbc-postgresql` | `postgresql` | BD PostgreSQL (prod) |
| `mapstruct` | `mapstruct` | Mapeo entre capas (igual en ambos) |
| `quarkus-junit5` | `spring-boot-starter-test` | Testing |
| `rest-assured` | `MockMvc` | Testing de endpoints REST |

---

## 🏛️ Arquitectura del módulo

Exactamente la misma arquitectura que Spring Boot — esta es la belleza de la Arquitectura Limpia:

```
┌─────────────────────────────────────────────────────────────┐
│                      INFRAESTRUCTURA                         │
│                                                             │
│   ┌──────────────┐         ┌──────────────────────────┐    │
│   │ ProductResource│        │ ProductPersistenceAdapter │    │
│   │  (JAX-RS)    │         │  (Panache / PostgreSQL)   │    │
│   └──────┬───────┘         └───────────────┬───────────┘    │
│          │ Puerto de entrada                │ Puerto de salida│
│   ┌──────▼──────────────────────────────────▼───────┐       │
│   │              APLICACIÓN (sin CDI)                │       │
│   │           ProductService (casos de uso)          │       │
│   └──────────────────────┬───────────────────────────┘       │
│                          │                                   │
│   ┌──────────────────────▼───────────────────────────┐       │
│   │              DOMINIO (sin frameworks)             │       │
│   │   Product (modelo) + Interfaces (puertos)         │       │
│   └───────────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 Estructura de carpetas

```
quarkus-product-service/
│
├── pom.xml                                    ← Quarkus BOM + extensiones
├── Dockerfile.jvm                             ← Docker (modo JVM)
├── QUARKUS.md                                 ← Esta guía
│
└── src/
    ├── main/
    │   ├── java/com/example/quarkusproduct/
    │   │   │
    │   │   ├── domain/                        ← Igual que Spring Boot
    │   │   │   ├── model/Product.java
    │   │   │   ├── port/in/ProductUseCase.java
    │   │   │   ├── port/out/ProductRepositoryPort.java
    │   │   │   └── exception/ProductNotFoundException.java
    │   │   │
    │   │   ├── application/service/
    │   │   │   └── ProductService.java        ← Sin @Service, sin @ApplicationScoped
    │   │   │
    │   │   └── infrastructure/
    │   │       ├── adapter/in/web/
    │   │       │   ├── ProductResource.java          ← @Path (JAX-RS)
    │   │       │   ├── ProductNotFoundExceptionMapper.java  ← @Provider
    │   │       │   └── dto/
    │   │       │       ├── ProductRequest.java
    │   │       │       ├── ProductResponse.java
    │   │       │       └── ProductMapper.java        ← componentModel = "cdi"
    │   │       ├── adapter/out/persistence/
    │   │       │   ├── ProductEntity.java            ← @Entity (idéntico a Spring Boot)
    │   │       │   ├── ProductJpaRepository.java     ← PanacheRepository
    │   │       │   ├── ProductPersistenceAdapter.java ← @ApplicationScoped
    │   │       │   └── ProductEntityMapper.java
    │   │       └── config/
    │   │           └── BeanProducer.java             ← @Produces (CDI)
    │   │
    │   └── resources/
    │       └── application.properties         ← Configuración multi-perfil
    │
    └── test/
        └── java/com/example/quarkusproduct/
            ├── application/service/
            │   └── ProductServiceTest.java    ← JUnit 5 + Mockito (igual a Spring Boot)
            └── infrastructure/adapter/in/web/
                └── ProductResourceIT.java     ← @QuarkusTest + REST Assured
```

---

## ▶️ Cómo ejecutar

### Requisitos previos
- Java 17+
- Maven 3.8+

### Modo Dev (con live reload 🔥)

```bash
cd quarkus-product-service

# Inicia el servidor con hot reload automático
./mvnw quarkus:dev
```

> 💡 **Dev Mode** es una de las mejores funcionalidades de Quarkus.
> Cada vez que cambias el código, Quarkus **recarga automáticamente** sin reiniciar.
> La aplicación estará disponible en: `http://localhost:8081`

### Modo normal

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

---

## 🌐 Endpoints de la API

Base URL: `http://localhost:8081/api/v1`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/products` | Listar todos los productos |
| `GET` | `/products/{id}` | Obtener un producto por ID |
| `POST` | `/products` | Crear un nuevo producto |
| `PUT` | `/products/{id}` | Actualizar un producto |
| `DELETE` | `/products/{id}` | Eliminar un producto |

### Swagger UI
```
http://localhost:8081/swagger-ui.html
```

### Health Check
```bash
curl http://localhost:8081/q/health
```

---

## 🔄 Diferencias clave: Spring Boot vs Quarkus

### 1. Anotaciones REST

```java
// Spring Boot (Spring MVC)
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @GetMapping
    public List<ProductResponse> findAll() { ... }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) { ... }
}

// Quarkus (JAX-RS estándar)
@Path("/api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @GET
    public List<ProductResponse> findAll() { ... }

    @POST
    public Response create(@Valid ProductRequest req) { ... }
}
```

---

### 2. Inyección de dependencias

```java
// Spring Boot (Spring IoC)
@Service
public class MyService {
    // Constructor injection con @RequiredArgsConstructor (Lombok)
    private final MyRepository repository;
}

// ---- O con @Bean en @Configuration ----
@Bean
public ProductUseCase productUseCase(ProductRepositoryPort repository) {
    return new ProductService(repository);
}

// Quarkus (CDI — Jakarta EE)
@ApplicationScoped
public class MyService {
    @Inject
    MyRepository repository;
}

// ---- O con @Produces en CDI ----
@Produces
@ApplicationScoped
public ProductUseCase productUseCase() {
    return new ProductService(repository);
}
```

---

### 3. Manejo de excepciones

```java
// Spring Boot
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleNotFound(ProductNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}

// Quarkus (JAX-RS ExceptionMapper)
@Provider
public class ProductNotFoundExceptionMapper
        implements ExceptionMapper<ProductNotFoundException> {

    @Override
    public Response toResponse(ProductNotFoundException ex) {
        return Response.status(404)
                .entity(Map.of("detail", ex.getMessage()))
                .build();
    }
}
```

---

### 4. Repositorio / Acceso a datos

```java
// Spring Boot (Spring Data JPA)
public interface ProductJpaRepository
        extends JpaRepository<ProductEntity, Long> {
    // Spring genera la implementación automáticamente
}

// Quarkus (Panache Repository Pattern)
@ApplicationScoped
public class ProductJpaRepository
        implements PanacheRepository<ProductEntity> {
    // Panache provee todos los métodos CRUD
    // Métodos disponibles: listAll(), findById(), persist(), delete(), count()...
}
```

---

### 5. Transacciones

```java
// Spring Boot
@Transactional  // de org.springframework.transaction.annotation
public Product save(Product product) { ... }

// Quarkus
@Transactional  // de jakarta.transaction.Transactional
public Product save(Product product) { ... }
```

---

### 6. Configuración multi-perfil

```yaml
# Spring Boot — application.yml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:h2:mem:productdb

---
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://...
```

```properties
# Quarkus — application.properties
# Perfil dev (activo por defecto con quarkus:dev)
%dev.quarkus.datasource.db-kind=h2
%dev.quarkus.datasource.jdbc.url=jdbc:h2:mem:productdb

# Perfil prod
%prod.quarkus.datasource.db-kind=postgresql
%prod.quarkus.datasource.jdbc.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
```

---

### 7. Testing

```java
// Spring Boot — test de capa web (contexto parcial)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean ProductUseCase productUseCase;  // mock del servicio

    @Test
    void GET_returns_ok() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
               .andExpect(status().isOk());
    }
}

// Quarkus — test de integración completo con REST Assured
@QuarkusTest
class ProductResourceIT {

    @Test
    void GET_returns_ok() {
        given()
            .when().get("/api/v1/products")
            .then().statusCode(200);
    }
}
```

---

## 💡 Conceptos importantes de Quarkus

### CDI — Contexts and Dependency Injection

CDI es el estándar Jakarta EE para inyección de dependencias. En Quarkus lo usas con:

| Anotación | Descripción |
|---|---|
| `@ApplicationScoped` | Un solo bean para toda la aplicación (equivale a `@Singleton`) |
| `@RequestScoped` | Un bean por petición HTTP |
| `@Singleton` | Similar a `@ApplicationScoped` pero más básico |
| `@Inject` | Inyectar una dependencia (equivale a `@Autowired`) |
| `@Produces` | Crear un bean con lógica personalizada (equivale a `@Bean`) |

### Panache — Acceso a datos simplificado

Panache reduce el boilerplate de JPA. Dos patrones:

**Repository Pattern** (usado en este proyecto — recomendado para Arquitectura Limpia):
```java
@ApplicationScoped
public class ProductJpaRepository implements PanacheRepository<ProductEntity> {
    // Métodos disponibles automáticamente:
    // listAll(), findById(), persist(), delete(), deleteById(), count()...
}
```

**Active Record Pattern** (más simple para proyectos pequeños):
```java
@Entity
public class Product extends PanacheEntity {
    public String name;

    // Métodos estáticos directamente en la entidad:
    // Product.findAll(), Product.findById(1L), product.persist()
}
```

### Profiles

| Perfil | Activación | Uso |
|---|---|---|
| `dev` | `quarkus:dev` | Desarrollo local con hot reload |
| `test` | `mvn test` | Tests automáticos |
| `prod` | Por defecto al empaquetar | Producción |

---

## 🐳 Ejecutar con Docker

```bash
# Desde la raíz del repositorio:

# Levanta Spring Boot (8080) + Quarkus (8081) + PostgreSQL
docker-compose up --build

# Solo Quarkus + PostgreSQL
docker-compose up postgres quarkus-product-service

# Ver logs de Quarkus
docker-compose logs -f quarkus-product-service
```

---

## 🧪 Ejecutar los tests

```bash
cd quarkus-product-service

# Ejecutar todos los tests
mvn test

# Solo tests unitarios (rápidos)
mvn test -Dtest=ProductServiceTest

# Solo tests de integración (requieren arrancar Quarkus)
mvn test -Dtest=ProductResourceIT
```

---

## 💪 Ejercicios para practicar

### Nivel Principiante 🟢

1. **Dev Mode**: Inicia `mvn quarkus:dev`, cambia el texto de un mensaje de log en `ProductService`
   y observa que el servidor se recarga automáticamente sin reiniciar.

2. **Agregar campo**: Agrega el campo `category` (String) al producto. Actualiza
   `Product`, `ProductEntity`, `ProductRequest`, `ProductResponse` y ambos mappers.

3. **Buscar por nombre**: Agrega en `ProductJpaRepository`:
   ```java
   public List<ProductEntity> findByName(String name) {
       return list("name like ?1", "%" + name + "%");
   }
   ```
   Y expón `GET /products/search?name=laptop` en `ProductResource`.

### Nivel Intermedio 🟡

4. **Paginación**: Modifica `listAll()` para usar `PanacheQuery` con `.page()`:
   ```java
   // En el repositorio
   public PanacheQuery<ProductEntity> findAllPaged() {
       return findAll();
   }
   ```

5. **Nuevo recurso**: Crea un `CategoryResource` con CRUD completo,
   siguiendo la misma arquitectura limpia.

6. **Reactive**: Cambia `ProductResource` para devolver `Uni<List<ProductResponse>>`
   usando RESTEasy Reactive de forma completamente reactiva.

### Nivel Avanzado 🔴

7. **Modo Nativo**: Compila el servicio a binario nativo con GraalVM y mide
   el tiempo de inicio (debería ser < 100ms).

8. **Quarkus + Kafka**: Agrega `quarkus-messaging-kafka` y publica un
   evento `ProductCreated` cuando se crea un producto.

9. **Panache + Reactive**: Usa `quarkus-hibernate-reactive-panache` para
   un stack completamente no bloqueante.

10. **Comparación de rendimiento**: Levanta ambos servicios (Spring Boot en 8080,
    Quarkus en 8081) y usa Apache Benchmark (`ab`) o `wrk` para comparar:
    - Tiempo de inicio
    - Uso de memoria en reposo
    - Requests por segundo bajo carga

---

## 🚀 Modo Nativo (GraalVM)

Una de las funcionalidades más potentes de Quarkus es compilar a binario nativo:

```bash
# Requiere GraalVM instalado y GRAALVM_HOME configurado
mvn package -Pnative

# Con Docker (sin necesidad de instalar GraalVM localmente)
mvn package -Pnative -Dquarkus.native.container-build=true

# Ejecutar el binario nativo (inicio en < 100ms)
./target/quarkus-product-service-0.0.1-SNAPSHOT-runner
```

> 💡 El binario nativo arranca en ~50ms y consume ~30MB de RAM,
> perfecto para funciones serverless y Kubernetes con escalado a cero.

---

## 📚 Recursos recomendados

- [Quarkus Getting Started Guide](https://quarkus.io/get-started/)
- [Quarkus Guides (todos los temas)](https://quarkus.io/guides/)
- [Panache — Simplificando JPA](https://quarkus.io/guides/hibernate-orm-panache)
- [RESTEasy Reactive](https://quarkus.io/guides/resteasy-reactive)
- [Quarkus Dev Services (BD automática en tests)](https://quarkus.io/guides/dev-services)
- [MicroProfile OpenAPI](https://quarkus.io/guides/openapi-swaggerui)

---

> 💡 **Tip**: Compara cada archivo de este módulo con su equivalente en la carpeta
> `src/` del módulo Spring Boot raíz. Las capas de dominio y aplicación son
> prácticamente idénticas — solo cambia la infraestructura. Eso es la Arquitectura Limpia.
