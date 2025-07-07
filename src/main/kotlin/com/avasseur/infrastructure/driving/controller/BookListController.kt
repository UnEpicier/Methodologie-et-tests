package com.avasseur.infrastructure.driving.controller

import com.avasseur.domain.model.Book
import com.avasseur.domain.usecase.BookListUseCase
import com.avasseur.infrastructure.driving.controller.dto.BookDTO
import com.avasseur.infrastructure.driving.controller.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @CrossOrigin
    @PatchMapping("/{id}")
    fun setBookedStateOfBook(@RequestBody booked: Boolean, @PathVariable id: String): ResponseEntity<Unit> {
        val book: Book? = bookListUseCase.getBookById(id.toInt())

        if (book == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        bookListUseCase.setBookedStateOfBook(book, booked)
        return ResponseEntity.status(201).build()
    }
}