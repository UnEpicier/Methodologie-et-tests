package com.avasseur.domain.usecase

import com.avasseur.domain.model.Book
import com.avasseur.domain.port.BookRepository

class BookListUseCase(val bookRepository: BookRepository) {
    fun getBooks() : List<Book> = bookRepository.getBooks().sortedBy { it.title }
    fun addBook(book: Book) {
        bookRepository.addBook(book)
    }
}