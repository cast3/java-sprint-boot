package com.example.productservice.domain.port.in;

import com.example.productservice.domain.model.Product;

import java.util.List;

/**
 * Puerto de entrada (Use Case): define los casos de uso disponibles para Product.
 * La capa de aplicación implementa esta interfaz.
 */
public interface ProductUseCase {

    List<Product> findAll();

    Product findById(Long id);

    Product create(Product product);

    Product update(Long id, Product product);

    void delete(Long id);
}
