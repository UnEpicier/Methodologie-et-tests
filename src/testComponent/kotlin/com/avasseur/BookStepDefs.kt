package com.avasseur

import io.cucumber.java.Before
import io.cucumber.java.ParameterType
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import io.restassured.response.ValidatableResponse
import org.springframework.boot.test.web.server.LocalServerPort

class BookStepDefs {
    @LocalServerPort
    private var port: Int? = 0

    @ParameterType("false|true")
    fun ghekinBoolean(value: String) = value.toBoolean()

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @When("the user creates the book {string} written by {string} and its reservation state is {ghekinBoolean}")
    fun createBook(title: String, author: String, booked: Boolean) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "title": "$title",
                      "author": "$author",
                      "booked": $booked
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }

    @When("the user get all books")
    fun getAllBooks() {
        lastBookResult = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
    }

    @Then("the list should contains the following books in the same order")
    fun shouldHaveListOfBooks(payload: List<Map<String, Any>>) {
        val expectedResponse = payload.joinToString(separator = ",", prefix = "[", postfix = "]") { line ->
            """
                ${
                line.entries.joinToString(separator = ",", prefix = "{", postfix = "}") {
                    val formattedValue = if (it.key == "booked") {
                        (it.value as String).toBoolean().toString()
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

    companion object {
        lateinit var lastBookResult: ValidatableResponse
    }
}