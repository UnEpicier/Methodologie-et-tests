package com.avasseur.infrastructure.driving.controller.dto

import com.avasseur.domain.model.Book

data class BookDTO(val title: String, val author: String, val booked: Boolean) {
    fun toDomain(): Book = Book(title, author, booked)
}

fun Book.toDto(): BookDTO = BookDTO(title, author, booked)
