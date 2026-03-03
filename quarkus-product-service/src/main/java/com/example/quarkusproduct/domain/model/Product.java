package com.example.quarkusproduct.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio Product.
 * Contiene únicamente lógica de negocio.
 * No depende de ningún framework (ni Spring, ni Quarkus, ni JPA).
 */
public class Product {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Integer stock;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.stock = builder.stock;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStock() { return stock; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    /** Verifica si hay stock disponible. */
    public boolean isAvailable() {
        return stock != null && stock > 0;
    }

    /** Verifica si el precio es válido (mayor que cero). */
    public boolean hasPriceGreaterThanZero() {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Builder fluido para construir instancias inmutables de Product. */
    public static final class Builder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder stock(Integer stock) { this.stock = stock; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Product build() { return new Product(this); }
    }
}
