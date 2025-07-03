package com.avasseur.infrastructure.driven.postgres

import com.avasseur.domain.model.Book
import com.avasseur.domain.port.BookRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BookRepository {
    override fun getBooks(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM BOOK", MapSqlParameterSource()) { rs, _ ->
                Book(
                    title = rs.getString("title"),
                    author = rs.getString("author"),
                    booked = rs.getBoolean("booked")
                )
            }
    }

    override fun addBook(book: Book) {
        namedParameterJdbcTemplate
            .update("INSERT INTO BOOK (title, author, booked) values (:title, :author, :booked)", mapOf(
                "title" to book.title,
                "author" to book.author,
                "booked" to book.booked
            ))
    }
}