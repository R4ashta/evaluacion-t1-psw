Feature: Registro de cursos
  Scenario: Registro de curso válido
    Given que el servicio de cursos está disponible
    When envío una solicitud para registrar el curso "Selenium Avanzado" con precio 400.0 y 20 vacantes
    Then el sistema responde con el código 200
    And el nombre del curso devuelto es "Selenium Avanzado"

  Scenario: Registro con precio inválido
    Given que el servicio de cursos está disponible
    When envío una solicitud para registrar el curso "Curso Inválido" con precio 0.0 y 15 vacantes
    Then el sistema rechaza el registro con un código de error