package com.pruebas.bdd.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RegistroCursoStepDefinitions {

    private Response response;

    @Given("que el servicio de cursos está disponible")
    public void queElServicioDeCursosEstaDisponible() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @When("envío una solicitud para registrar el curso {string} con precio {double} y {int} vacantes")
    public void envioUnaSolicitudParaRegistrarElCurso(String nombre, Double precio, Integer vacantes) {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", nombre);
        body.put("precio", precio);
        body.put("vacantes", vacantes);

        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/cursos");
    }

    @Then("el sistema responde con código de estado {int}")
    public void elSistemaRespondeConCodigoDeEstado(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }

    @And("el nombre del curso devuelto es {string}")
    public void elNombreDelCursoDevueltoEs(String nombreEsperado) {
        response.then().body("nombre", equalTo(nombreEsperado));
    }

    @Then("el sistema rechaza el registro con un código de error de cliente o servidor")
    public void elSistemaRechazaElRegistroConUnCodigoDeError() {
        int status = response.getStatusCode();
        assertTrue(status >= 400 && status < 600,
                "Se esperaba un código 4xx o 5xx, pero se recibió: " + status);
    }
}