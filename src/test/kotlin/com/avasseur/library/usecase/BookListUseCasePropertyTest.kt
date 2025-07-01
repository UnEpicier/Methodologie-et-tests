package com.avasseur.library.usecase

import com.avasseur.library.model.Book
import com.avasseur.library.port.BookRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.checkAll

class InMemoryBookRepository : BookRepository {
    private val books = mutableListOf<Book>()

    override fun getAll(): List<Book> = books

    override fun addBook(book: Book) {
        books.add(book)
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
            bookRepository.addBook(Book("Author", title))
            titles.add(title)
        }

        bookListUseCase.getBooks().map { it.name } shouldBe  titles.sorted()
    }
})