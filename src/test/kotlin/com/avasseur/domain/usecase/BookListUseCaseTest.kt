package com.avasseur.domain.usecase

import com.avasseur.domain.model.Book
import com.avasseur.domain.port.BookRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class BookListUseCaseTest : StringSpec({
    val bookRepository = mockk<BookRepository>()
    val bookListUseCase = BookListUseCase(bookRepository)

    "getAll returns sorted list of books" {
        // Arrange
        val books = listOf(
            Book("Author1", "Book1"),
            Book("Author3", "Book3"),
            Book("Author2", "Book2")
        )
        every { bookRepository.getBooks() } returns books

        // Act
        val result = bookListUseCase.getBooks()

        // Assert
        result shouldContainExactly listOf(
            Book("Author1", "Book1"),
            Book("Author2", "Book2"),
            Book("Author3", "Book3")
        )
    }

    "getById should return the correct book" {
        // Arrange
        val book = Book(1, "Author1", "Book1")
        every { bookRepository.getBookById(1) } returns book

        // Act
        val result = bookListUseCase.getBookById(1)

        // Assert
        result shouldBe book
    }

    "add method adds a book to the repository" {
        // Arrange
        val book = Book("NewAuthor", "NewTitle")
        justRun { bookRepository.addBook(any()) }

        // Act
        bookListUseCase.addBook(book)

        // Assert
        verify(exactly = 1) { bookRepository.addBook(book) }
    }

    "setBookedStateOfBook should update the booked state of a book" {
        // Arrange
        val book = Book(1, "A", "B")
        val newBookedState = true

        justRun { bookRepository.setBookedStateOfBook(book, newBookedState) }

        // Act
        bookListUseCase.setBookedStateOfBook(book, newBookedState)

        // Assert
        verify(exactly = 1) { bookRepository.setBookedStateOfBook(book, newBookedState) }
    }

})