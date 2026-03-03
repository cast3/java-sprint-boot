package com.example.quarkusproduct.infrastructure.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio Panache para ProductEntity.
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: interface extiende JpaRepository<ProductEntity, Long>
 *                  Spring genera la implementación automáticamente
 *   - Quarkus:     clase implementa PanacheRepository<ProductEntity>
 *                  Panache provee findAll(), findById(), persist(), delete(), count(), etc.
 *                  Necesita @ApplicationScoped para ser gestionada por CDI
 *
 * PanacheRepository hereda métodos como:
 *   findAll(), findById(id), persist(entity), delete(entity), deleteById(id),
 *   count(), listAll(), stream(), etc.
 */
@ApplicationScoped
public class ProductJpaRepository implements PanacheRepository<ProductEntity> {
    // Panache provee todos los métodos CRUD automáticamente.
    // Aquí puedes agregar queries personalizados con JPQL:
    // public List<ProductEntity> findByName(String name) {
    //     return list("name like ?1", "%" + name + "%");
    // }
}
