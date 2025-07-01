package com.avasseur.library.port

import com.avasseur.library.model.Book

interface BookRepository {
    fun addBook(book: Book)
    fun getAll(): List<Book>
}