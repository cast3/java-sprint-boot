package com.example.quarkusproduct.domain.port.out;

import com.example.quarkusproduct.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Repository Port): define cómo la aplicación accede a la persistencia.
 * La capa de infraestructura implementa esta interfaz.
 */
public interface ProductRepositoryPort {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    boolean existsById(Long id);
}
