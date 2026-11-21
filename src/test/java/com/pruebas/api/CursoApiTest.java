package com.pruebas.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CursoApiTest {

    private static final String ALUMNO = "Jimmy John Huo Pérez Espinoza";
    private static final String CODIGO = "202504498";

    @BeforeAll
    static void configurarApi() {
        System.out.println("Alumno: " + ALUMNO + " Código: " + CODIGO);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void consultarTodosLosCursos() {
        given()
                .when()
                .get("/api/cursos")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(3));
    }

    @Test
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