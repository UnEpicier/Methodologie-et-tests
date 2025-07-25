package com.avasseur.infrastructure.driving.controller

import com.avasseur.domain.model.Book
import com.avasseur.domain.usecase.BookListUseCase
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@WebMvcTest
class BookListControllerITest(
    @MockkBean private val bookListUseCase: BookListUseCase,
    private val mockMvc: MockMvc
) : StringSpec({
    extension(SpringExtension)

    "rest route get books" {
        // GIVEN
        every { bookListUseCase.getBooks() } returns listOf(Book("A", "B"))

        // WHEN
        mockMvc.get("/books")
            //THEN
            .andExpect {
                status { isOk() }
                content { content { APPLICATION_JSON } }
                content {
                    json(
                        // language=json
                        """
                        [
                          {
                            "title": "A",
                            "author": "B"
                          }
                        ]
                        """.trimIndent()
                    )
                }
            }
    }

    "rest route post book" {
        justRun { bookListUseCase.addBook(any()) }

        mockMvc.post("/books") {
            // language=json
            content = """
                {
                  "title": "Les misérables",
                  "author": "Victor Hugo"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
        }

        val expected = Book(
            title = "Les misérables",
            author = "Victor Hugo"
        )

        verify(exactly = 1) { bookListUseCase.addBook(expected) }
    }

    "rest route post book should return 400 when body is not good" {
        justRun { bookListUseCase.addBook(any()) }

        mockMvc.post("/books") {
            // language=json
            content = """
                {
                  "name": "Les misérables",
                  "author": "Victor Hugo"
                }
            """.trimIndent()
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }

        verify(exactly = 0) { bookListUseCase.addBook(any()) }
    }

    "rest route patch book" {
        // GIVEN
        every { bookListUseCase.getBookById(1) } returns Book("A", "B")
        justRun { bookListUseCase.setBookedStateOfBook(any(), any()) }

        // WHEN
        mockMvc.patch("/books/1") {
            // language=json
            content = "true"
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
        }

        verify(exactly = 1) { bookListUseCase.setBookedStateOfBook(any(), any()) }
    }

    "rest route patch book should return 404 when book not found" {
        // GIVEN
        every { bookListUseCase.getBookById(99) } returns null

        // WHEN
        mockMvc.patch("/books/99") {
            // language=json
            content = "true"
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }
            //THEN
            .andExpect {
                status { isNotFound() }
            }

        verify(exactly = 0) { bookListUseCase.setBookedStateOfBook(any(), any()) }
    }

    "rest route patch book return 400 when body is not good" {
        // GIVEN
        every { bookListUseCase.getBookById(99) } returns null

        // WHEN
        mockMvc.patch("/books/1")
            //THEN
            .andExpect {
                status { isBadRequest() }
            }
    }
})