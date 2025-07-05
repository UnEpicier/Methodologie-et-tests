package com.avasseur.infrastructure.driving.controller.dto

import com.avasseur.domain.model.Book

data class BookDTO(val id: Int?, val title: String, val author: String, val booked: Boolean) {
    fun toDomain(): Book = Book(id, title, author, booked)
}

fun Book.toDto(): BookDTO = BookDTO(id, title, author, booked)
