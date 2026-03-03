package com.example.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio Product.
 * Contiene únicamente lógica de negocio y no depende de ningún framework.
 */
@Getter
@Builder
@AllArgsConstructor
public class Product {

    private final Long id;
    private final String name;
    private final String description;

    @With
    private final BigDecimal price;

    private final Integer stock;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /** Verifica si hay stock disponible. */
    public boolean isAvailable() {
        return stock != null && stock > 0;
    }

    /** Verifica si el precio es válido (mayor que cero). */
    public boolean hasPriceGreaterThanZero() {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
}
