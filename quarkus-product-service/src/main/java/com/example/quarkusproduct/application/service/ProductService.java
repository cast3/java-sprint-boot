package com.example.quarkusproduct.application.service;

import com.example.quarkusproduct.domain.exception.ProductNotFoundException;
import com.example.quarkusproduct.domain.model.Product;
import com.example.quarkusproduct.domain.port.in.ProductUseCase;
import com.example.quarkusproduct.domain.port.out.ProductRepositoryPort;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación: orquesta la lógica de negocio usando los puertos.
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: @Slf4j (Lombok) para logging
 *   - Quarkus:     Logger de JBoss Logging (incluido en Quarkus)
 *
 * Esta clase NO tiene anotaciones de framework (@ApplicationScoped, @Service, etc.)
 * Se instancia desde BeanProducer, igual que en la versión Spring Boot.
 */
public class ProductService implements ProductUseCase {

    private static final Logger log = Logger.getLogger(ProductService.class);

    private final ProductRepositoryPort repository;

    public ProductService(ProductRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> findAll() {
        log.info("Obteniendo todos los productos");
        return repository.findAll();
    }

    @Override
    public Product findById(Long id) {
        log.infof("Buscando producto con id: %d", id);
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product create(Product product) {
        log.infof("Creando producto: %s", product.getName());
        Product productToSave = Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return repository.save(productToSave);
    }

    @Override
    public Product update(Long id, Product product) {
        log.infof("Actualizando producto con id: %d", id);
        Product existing = findById(id);
        Product updated = Product.builder()
                .id(existing.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        log.infof("Eliminando producto con id: %d", id);
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
