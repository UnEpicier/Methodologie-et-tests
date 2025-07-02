package com.avasseur.infrastructure.driving.controller.dto

import com.avasseur.domain.model.Book

data class BookDTO(val title: String, val author: String) {
    fun toDomain(): Book = Book(title, author)
}

fun Book.toDto(): BookDTO = BookDTO(title, author)
