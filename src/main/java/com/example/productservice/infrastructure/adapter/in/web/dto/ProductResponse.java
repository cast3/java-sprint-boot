package com.example.productservice.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de salida con los datos de un producto.
 */
@Data
@Builder
@Schema(description = "Datos de un producto")
public class ProductResponse {

    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Laptop Gaming")
    private String name;

    @Schema(description = "Descripción del producto")
    private String description;

    @Schema(description = "Precio del producto", example = "1299.99")
    private BigDecimal price;

    @Schema(description = "Cantidad en stock", example = "50")
    private Integer stock;

    @Schema(description = "Indica si hay stock disponible")
    private boolean available;

    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    private LocalDateTime updatedAt;
}
