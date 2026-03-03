package com.example.productservice.application.service;

import com.example.productservice.domain.exception.ProductNotFoundException;
import com.example.productservice.domain.model.Product;
import com.example.productservice.domain.port.in.ProductUseCase;
import com.example.productservice.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación: orquesta la lógica de negocio usando los puertos.
 * No depende de Spring Web ni de JPA — solo de interfaces de dominio.
 */
@Slf4j
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort repository;

    @Override
    public List<Product> findAll() {
        log.info("Obteniendo todos los productos");
        return repository.findAll();
    }

    @Override
    public Product findById(Long id) {
        log.info("Buscando producto con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product create(Product product) {
        log.info("Creando producto: {}", product.getName());
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
        log.info("Actualizando producto con id: {}", id);
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
        log.info("Eliminando producto con id: {}", id);
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
