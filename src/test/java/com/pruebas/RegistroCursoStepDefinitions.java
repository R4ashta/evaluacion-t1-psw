package com.pruebas.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class RegistroCursoStepDefinitions {

    private static final String ALUMNO = "Jimmy John Huo Pérez Espinoza";
    private static final String CODIGO = "202504498";

    private Response respuesta;

    @Before
    public void prepararEscenario() {
        System.out.println("Alumno: " + ALUMNO + " Código: " + CODIGO);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Given("que el servicio de cursos está disponible")
    public void queElServicioDeCursosEstaDisponible() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @When("envío una solicitud para registrar el curso {string} con precio {double} y {int} vacantes")
    public void envioUnaSolicitudParaRegistrarElCurso(String nombre, Double precio, Integer vacantes) {
        String body = """
                {
                    "nombre": "%s",
                    "precio": %s,
                    "vacantes": %d
                }
                """.formatted(nombre, precio, vacantes);

        respuesta = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/cursos");
    }

    @Then("el sistema responde con el código {int}")
    public void elSistemaRespondeConElCodigo(int codigo) {
        respuesta.then().statusCode(codigo);
    }

    @And("el nombre del curso devuelto es {string}")
    public void elNombreDelCursoDevueltoEs(String nombreEsperado) {
        respuesta.then().body("nombre", equalTo(nombreEsperado));
    }

    @Then("el sistema rechaza el registro con un código de error")
    public void elSistemaRechazaElRegistroConUnCodigoDeError() {
        respuesta.then().statusCode(greaterThanOrEqualTo(400));
    }
}