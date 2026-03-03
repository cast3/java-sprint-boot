package com.example.productservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA Spring Data para ProductEntity.
 * Spring genera la implementación automáticamente.
 */
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
