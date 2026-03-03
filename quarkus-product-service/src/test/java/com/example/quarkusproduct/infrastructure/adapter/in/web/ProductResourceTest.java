package com.example.quarkusproduct.infrastructure.adapter.in.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test de integración para ProductResource.
 * Usa @QuarkusTest para iniciar la aplicación completa con H2 en memoria.
 *
 * DIFERENCIA vs Spring Boot:
 *   - Spring Boot: @WebMvcTest (contexto parcial) + @MockBean (mocks de dependencias)
 *   - Quarkus:     @QuarkusTest (aplicación completa) + REST Assured (cliente HTTP)
 *
 * Los tests de Quarkus son más cercanos a tests de integración reales
 * porque inician todo el stack (incluyendo la BD H2).
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductResourceTest {

    private static final String BASE_PATH = "/api/v1/products";

    @Test
    @Order(1)
    void GET_products_returnsEmptyListOrMore() {
        given()
                .when().get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    @Order(2)
    void POST_product_validRequest_returnsCreated() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "Laptop Gaming",
                          "description": "Laptop de alto rendimiento para gaming",
                          "price": 1299.99,
                          "stock": 50
                        }
                        """)
                .when().post(BASE_PATH)
                .then()
                .statusCode(201)
                .body("name", equalTo("Laptop Gaming"))
                .body("id", notNullValue())
                .body("available", equalTo(true));
    }

    @Test
    @Order(3)
    void POST_product_invalidRequest_returns400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "name": "",
                          "price": -5,
                          "stock": -1
                        }
                        """)
                .when().post(BASE_PATH)
                .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    void GET_products_byId_notFound_returns404() {
        given()
                .when().get(BASE_PATH + "/9999")
                .then()
                .statusCode(404)
                .body("title", equalTo("Recurso no encontrado"));
    }

    @Test
    @Order(5)
    void DELETE_product_notFound_returns404() {
        given()
                .when().delete(BASE_PATH + "/9999")
                .then()
                .statusCode(404);
    }
}
