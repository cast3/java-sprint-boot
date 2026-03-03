package com.example.quarkusproduct.infrastructure.config;

import com.example.quarkusproduct.application.service.ProductService;
import com.example.quarkusproduct.domain.port.in.ProductUseCase;
import com.example.quarkusproduct.domain.port.out.ProductRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

/**
 * Productor CDI: conecta las capas sin que ProductService conozca CDI.
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: @Configuration + @Bean
 *   - Quarkus:     @ApplicationScoped + @Produces (CDI)
 *
 * Ambos enfoques cumplen el mismo objetivo:
 *   instanciar ProductService pasándole su dependencia manualmente,
 *   manteniendo ProductService libre de anotaciones de framework.
 */
@ApplicationScoped
public class BeanProducer {

    @Inject
    ProductRepositoryPort repository;

    @Produces
    @ApplicationScoped
    public ProductUseCase productUseCase() {
        return new ProductService(repository);
    }
}
