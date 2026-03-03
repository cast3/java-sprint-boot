package com.example.productservice.infrastructure.adapter.in.web;

import com.example.productservice.domain.exception.ProductNotFoundException;
import com.example.productservice.domain.model.Product;
import com.example.productservice.domain.port.in.ProductUseCase;
import com.example.productservice.infrastructure.adapter.in.web.dto.ProductMapper;
import com.example.productservice.infrastructure.adapter.in.web.dto.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductUseCase productUseCase;

    @MockBean
    private ProductMapper productMapper;

    private Product sampleProduct() {
        return Product.builder()
                .id(1L)
                .name("Laptop Gaming")
                .description("Laptop de alto rendimiento")
                .price(new BigDecimal("1299.99"))
                .stock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ProductResponse sampleResponse() {
        return ProductResponse.builder()
                .id(1L)
                .name("Laptop Gaming")
                .description("Laptop de alto rendimiento")
                .price(new BigDecimal("1299.99"))
                .stock(10)
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void GET_products_returnsOk() throws Exception {
        when(productUseCase.findAll()).thenReturn(List.of(sampleProduct()));
        when(productMapper.toResponse(any())).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop Gaming"));
    }

    @Test
    void GET_products_byId_returnsOk() throws Exception {
        when(productUseCase.findById(1L)).thenReturn(sampleProduct());
        when(productMapper.toResponse(any())).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void GET_products_byId_notFound_returns404() throws Exception {
        when(productUseCase.findById(99L)).thenThrow(new ProductNotFoundException(99L));

        mockMvc.perform(get("/api/v1/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_products_validRequest_returnsCreated() throws Exception {
        String requestBody = """
                {
                  "name": "Laptop Gaming",
                  "description": "Laptop de alto rendimiento",
                  "price": 1299.99,
                  "stock": 10
                }
                """;

        when(productMapper.toDomain(any())).thenReturn(sampleProduct());
        when(productUseCase.create(any())).thenReturn(sampleProduct());
        when(productMapper.toResponse(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop Gaming"));
    }

    @Test
    void POST_products_invalidRequest_returns400() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "price": -5,
                  "stock": -1
                }
                """;

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void PUT_products_byId_returnsOk() throws Exception {
        String requestBody = """
                {
                  "name": "Laptop Gaming Pro",
                  "description": "Versión mejorada",
                  "price": 1599.99,
                  "stock": 5
                }
                """;

        when(productMapper.toDomain(any())).thenReturn(sampleProduct());
        when(productUseCase.update(eq(1L), any())).thenReturn(sampleProduct());
        when(productMapper.toResponse(any())).thenReturn(sampleResponse());

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void DELETE_products_byId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETE_products_byId_notFound_returns404() throws Exception {
        doThrow(new ProductNotFoundException(99L)).when(productUseCase).delete(99L);

        mockMvc.perform(delete("/api/v1/products/99"))
                .andExpect(status().isNotFound());
    }
}
