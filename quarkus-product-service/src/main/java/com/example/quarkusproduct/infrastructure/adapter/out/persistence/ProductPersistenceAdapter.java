package com.example.quarkusproduct.infrastructure.adapter.out.persistence;

import com.example.quarkusproduct.domain.model.Product;
import com.example.quarkusproduct.domain.port.out.ProductRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida: implementa ProductRepositoryPort usando Panache (JPA).
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: @Component — registrado automáticamente como bean de Spring
 *   - Quarkus:     @ApplicationScoped — registrado como bean CDI
 *
 *   - Spring Boot: @Transactional de Spring (org.springframework.transaction.annotation)
 *   - Quarkus:     @Transactional de Jakarta EE (jakarta.transaction.Transactional)
 */
@ApplicationScoped
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    @Inject
    ProductJpaRepository jpaRepository;

    @Inject
    ProductEntityMapper entityMapper;

    @Override
    public List<Product> findAll() {
        return jpaRepository.listAll().stream()
                .map(entityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findByIdOptional(id)
                .map(entityMapper::toDomain);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity entity = entityMapper.toEntity(product);
        if (entity.getId() == null) {
            jpaRepository.persist(entity);
        } else {
            entity = jpaRepository.getEntityManager().merge(entity);
        }
        return entityMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.findByIdOptional(id).isPresent();
    }
}
