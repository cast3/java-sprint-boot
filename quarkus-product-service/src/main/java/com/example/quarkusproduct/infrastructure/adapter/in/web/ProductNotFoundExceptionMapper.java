package com.example.quarkusproduct.infrastructure.adapter.in.web;

import com.example.quarkusproduct.domain.exception.ProductNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Map;

/**
 * Manejador de ProductNotFoundException: convierte la excepción en HTTP 404.
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: @RestControllerAdvice + @ExceptionHandler
 *   - Quarkus:     @Provider + ExceptionMapper<T> (JAX-RS estándar)
 */
@Provider
public class ProductNotFoundExceptionMapper implements ExceptionMapper<ProductNotFoundException> {

    private static final Logger log = Logger.getLogger(ProductNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(ProductNotFoundException exception) {
        log.warnf("Producto no encontrado: %s", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "title", "Recurso no encontrado",
                        "detail", exception.getMessage(),
                        "status", 404
                ))
                .build();
    }
}
