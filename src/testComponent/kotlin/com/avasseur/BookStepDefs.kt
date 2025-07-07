package com.avasseur

import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import io.restassured.response.ValidatableResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate

class BookStepDefs {
    @LocalServerPort
    private var port: Int? = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        jdbcTemplate.execute("TRUNCATE TABLE book RESTART IDENTITY CASCADE")
    }

    @When("the user creates the book {string} written by {string}")
    fun createBook(title: String, author: String) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "title": "$title",
                      "author": "$author"
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }

    @When("the user gets all books")
    fun getAllBooks() {
        lastBookResult = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
    }

    @When("the user creates the book with id {int}")
    fun actionBook(action: String, id: Int) {
        if (action == "get") {
            lastBookResult = given()
                .`when`()
                .get("/books/$id")
                .then()
            return
        }

        val bookedStatus = action == "books"
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(bookedStatus.toString())
            .`when`()
            .patch("/books/$id")
            .then()
            .statusCode(201)
    }

    @Then("the list should contains the following books in the same order")
    fun shouldHaveListOfBooks(payload: List<Map<String, Any>>) {
        val expectedResponse = payload.joinToString(separator = ",", prefix = "[", postfix = "]") { line ->
            """
                ${
                line.entries.joinToString(separator = ",", prefix = "{", postfix = "}") {
                    val formattedValue = if (it.key == "booked") {
                        (it.value as String).toBoolean().toString()
                    } else if (it.key == "id") {
                        (it.value as String).toIntOrNull().toString()
                    } else {
                        """"${it.value}""""
                    }

                    """"${it.key}": $formattedValue"""
                }
            }
            """.trimIndent()

        }
        lastBookResult.extract().body().jsonPath().prettify() shouldBe JsonPath(expectedResponse).prettify()
    }

    @When("the user books the book with id {int}")
    fun bookABook(id: Int) {
        given()
            .contentType(ContentType.JSON)
            .body("true")
            .`when`()
            .patch("/books/$id")
            .then()
            .statusCode(201)
    }

    @When("the user unbooks the book with id {int}")
    fun unbookABook(id: Int) {
        given()
            .contentType(ContentType.JSON)
            .body("false")
            .`when`()
            .patch("/books/$id")
            .then()
            .statusCode(201)
    }

    @When("the user tries to book a non-existing book with id {int}")
    fun bookNonExistingBook(id: Int) {
        lastBookResult = given()
            .contentType(ContentType.JSON)
            .body("true")
            .`when`()
            .patch("/books/$id")
            .then()
    }

    @Then("the booking request should fail with status code {int}")
    fun bookingShouldFailWith(code: Int) {
        lastBookResult.statusCode(code)
    }


    companion object {
        lateinit var lastBookResult: ValidatableResponse
    }
}