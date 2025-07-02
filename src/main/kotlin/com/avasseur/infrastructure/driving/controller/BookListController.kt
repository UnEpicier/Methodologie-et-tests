package com.avasseur.infrastructure.driving.controller

import com.avasseur.domain.usecase.BookListUseCase
import com.avasseur.infrastructure.driving.controller.dto.BookDTO
import com.avasseur.infrastructure.driving.controller.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookListController(private val bookListUseCase: BookListUseCase) {

    @CrossOrigin
    @GetMapping
    fun getBooks(): List<BookDTO> {
        return bookListUseCase.getBooks().map { it.toDto() }
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody request: BookDTO) {
        bookListUseCase.addBook(request.toDomain())
    }
}