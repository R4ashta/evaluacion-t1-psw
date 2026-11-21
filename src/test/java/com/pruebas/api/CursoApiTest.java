package com.pruebas.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

class CursoApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @DisplayName("Escenario 1: Consultar todos los cursos")
    void consultarTodosLosCursos() {
        given()
                .when()
                .get("/api/cursos")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(3));
    }

    @Test
    @DisplayName("Escenario 2: Consultar el curso por id")
    void consultarCursoPorId() {
        given()
                .pathParam("id", 2)
                .when()
                .get("/api/cursos/{id}")
                .then()
                .statusCode(200)
                .body("nombre", equalTo("Seguridad de Aplicaciones"))
                .body("vacantes", greaterThan(0));
    }
}