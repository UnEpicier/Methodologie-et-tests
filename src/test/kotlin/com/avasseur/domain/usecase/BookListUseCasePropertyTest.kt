package com.avasseur.domain.usecase

import com.avasseur.domain.model.Book
import com.avasseur.domain.port.BookRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.property.Arb
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.checkAll

class InMemoryBookRepository : BookRepository {
    private val books = mutableListOf<Book>()

    override fun getBooks(): List<Book> = books

    override fun getBookById(id: Int): Book? = books.find { it.id == id }

    override fun addBook(book: Book) {
        books.add(book)
    }

    override fun setBookedStateOfBook(book: Book, booked: Boolean) {
        val newBook = Book(
            id = book.id,
            title = book.title,
            author = book.author,
            booked = booked
        )

        books.remove(book)
        books.add(newBook)
    }

    fun clear() {
        books.clear()
    }
}

class BookListUseCasePropertyTest : StringSpec({
    val bookRepository = InMemoryBookRepository()
    val bookListUseCase = BookListUseCase(bookRepository)

    "should return all elements in the alphabetical order" {
        bookRepository.clear()
        val titles = mutableListOf<String>()

        checkAll(Arb.stringPattern("[A-Za-z]+")) { title ->
            titles.add(title)
            bookRepository.addBook(Book(title, "Self"))
        }

        bookListUseCase.getBooks().map { it.title } shouldContainExactly titles.sorted()
    }
})