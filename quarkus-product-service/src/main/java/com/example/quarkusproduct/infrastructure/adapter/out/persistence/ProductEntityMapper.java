package com.example.quarkusproduct.infrastructure.adapter.out.persistence;

import com.example.quarkusproduct.domain.model.Product;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct para convertir entre Product (dominio) y ProductEntity (JPA).
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: componentModel = "spring"
 *   - Quarkus:     componentModel = "cdi"
 */
@Mapper(componentModel = "cdi")
public interface ProductEntityMapper {

    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity entity);
}
