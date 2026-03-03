package com.example.productservice.application.service;

import com.example.productservice.domain.exception.ProductNotFoundException;
import com.example.productservice.domain.model.Product;
import com.example.productservice.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort repository;

    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        productService = new ProductService(repository);
        sampleProduct = Product.builder()
                .id(1L)
                .name("Laptop Gaming")
                .description("Laptop de alto rendimiento")
                .price(new BigDecimal("1299.99"))
                .stock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_returnsListOfProducts() {
        when(repository.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> result = productService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop Gaming");
    }

    @Test
    void findById_withExistingId_returnsProduct() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop Gaming");
    }

    @Test
    void findById_withNonExistingId_throwsProductNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_savesAndReturnsProduct() {
        Product input = Product.builder()
                .name("Teclado Mecánico")
                .description("Teclado con switches Cherry MX")
                .price(new BigDecimal("150.00"))
                .stock(20)
                .build();

        when(repository.save(any(Product.class))).thenReturn(sampleProduct);

        Product result = productService.create(input);

        verify(repository).save(any(Product.class));
        assertThat(result).isNotNull();
    }

    @Test
    void update_withExistingId_updatesProduct() {
        Product updatedData = Product.builder()
                .name("Laptop Gaming Pro")
                .description("Versión mejorada")
                .price(new BigDecimal("1599.99"))
                .stock(5)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.update(1L, updatedData);

        assertThat(result.getName()).isEqualTo("Laptop Gaming Pro");
        assertThat(result.getPrice()).isEqualByComparingTo("1599.99");
    }

    @Test
    void delete_withExistingId_deletesProduct() {
        when(repository.existsById(1L)).thenReturn(true);

        productService.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_withNonExistingId_throwsProductNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> productService.delete(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void product_isAvailable_whenStockGreaterThanZero() {
        assertThat(sampleProduct.isAvailable()).isTrue();
    }

    @Test
    void product_isNotAvailable_whenStockIsZero() {
        Product outOfStock = Product.builder()
                .id(2L)
                .name("Producto Agotado")
                .price(BigDecimal.TEN)
                .stock(0)
                .build();

        assertThat(outOfStock.isAvailable()).isFalse();
    }
}
