package com.example.productservice.infrastructure.config;

import com.example.productservice.application.service.ProductService;
import com.example.productservice.domain.port.in.ProductUseCase;
import com.example.productservice.domain.port.out.ProductRepositoryPort;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración central: registra los beans de la capa de aplicación
 * y conecta las dependencias siguiendo los principios de inversión de control.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public ProductUseCase productUseCase(ProductRepositoryPort repository) {
        return new ProductService(repository);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service API")
                        .version("1.0.0")
                        .description("Microservicio de productos con Arquitectura Limpia y Spring Boot")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu@email.com")));
    }
}
