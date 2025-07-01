package com.avasseur.library.usecase

import com.avasseur.library.model.Book
import com.avasseur.library.port.BookRepository

class BookListUseCase(val bookRepository: BookRepository) {
    fun getBooks() : List<Book> = bookRepository.getAll().sortedBy { it.name }
    fun addBook(book: Book) {
        bookRepository.addBook(book)
    }
}