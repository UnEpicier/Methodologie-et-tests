package com.avasseur.domain.usecase
import com.avasseur.domain.model.Book
import com.avasseur.domain.port.BookRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class BookListUseCaseTest: StringSpec ({
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

    "add method adds a book to the repository" {
        // Arrange
        val book = Book("NewAuthor", "NewTitle")
        justRun { bookRepository.addBook(any()) }

        // Act
        bookListUseCase.addBook(book)

        // Assert
        verify(exactly = 1) { bookRepository.addBook(book) }
    }
})