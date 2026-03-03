package com.example.quarkusproduct.infrastructure.adapter.in.web.dto;

import com.example.quarkusproduct.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct para convertir entre Product (dominio) y DTOs.
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: componentModel = "spring"
 *   - Quarkus:     componentModel = "cdi"  (CDI en lugar de Spring IoC)
 */
@Mapper(componentModel = "cdi")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toDomain(ProductRequest request);

    @Mapping(target = "available", expression = "java(product.isAvailable())")
    ProductResponse toResponse(Product product);
}
