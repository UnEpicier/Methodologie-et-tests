package com.avasseur.domain.port

import com.avasseur.domain.model.Book

interface BookRepository {
    fun getBooks(): List<Book>
    fun addBook(book: Book)
}