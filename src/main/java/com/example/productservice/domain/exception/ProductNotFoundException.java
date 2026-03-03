package com.example.productservice.domain.exception;

/**
 * Excepción de dominio: se lanza cuando un producto no existe.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Producto no encontrado con id: " + id);
    }
}
