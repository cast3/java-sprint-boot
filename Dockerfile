# ---- Etapa 1: Construcción ----
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de Maven primero (para aprovechar la caché de Docker)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiar el código fuente y construir
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ---- Etapa 2: Imagen final (más ligera) ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Usuario no root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiar el JAR generado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
