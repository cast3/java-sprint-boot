package com.example.productservice.infrastructure.adapter.out.persistence;

import com.example.productservice.domain.model.Product;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct para convertir entre Product (dominio) y ProductEntity (JPA).
 */
@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity entity);
}
