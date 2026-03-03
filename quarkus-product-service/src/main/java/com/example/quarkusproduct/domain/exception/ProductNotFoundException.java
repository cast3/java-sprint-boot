package com.example.quarkusproduct.domain.exception;

/**
 * Excepción de dominio: se lanza cuando un producto no existe.
 * No depende de ningún framework.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Producto no encontrado con id: " + id);
    }
}
