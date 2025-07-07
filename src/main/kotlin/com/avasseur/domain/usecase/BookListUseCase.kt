package com.avasseur.domain.usecase

import com.avasseur.domain.model.Book
import com.avasseur.domain.port.BookRepository

class BookListUseCase(val bookRepository: BookRepository) {
    fun getBooks(): List<Book> = bookRepository.getBooks().sortedBy { it.title }
    fun getBookById(id: Int): Book? = bookRepository.getBookById(id)
    fun addBook(book: Book) {
        bookRepository.addBook(book)
    }

    fun setBookedStateOfBook(book: Book, booked: Boolean) {
        bookRepository.setBookedStateOfBook(book, booked)
    }
}