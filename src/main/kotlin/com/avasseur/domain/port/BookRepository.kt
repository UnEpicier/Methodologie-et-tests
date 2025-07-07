package com.avasseur.domain.port

import com.avasseur.domain.model.Book

interface BookRepository {
    fun getBooks(): List<Book>
    fun getBookById(id: Int): Book?
    fun addBook(book: Book)
    fun setBookedStateOfBook(book: Book, booked: Boolean)
}