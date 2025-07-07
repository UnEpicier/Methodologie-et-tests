package com.avasseur.infrastructure.driven.postgres

import com.avasseur.domain.model.Book
import com.avasseur.domain.port.BookRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookRepository {
    override fun getBooks(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM book", MapSqlParameterSource()) { rs, _ ->
                Book(
                    id = rs.getInt("id"),
                    title = rs.getString("title"),
                    author = rs.getString("author"),
                    booked = rs.getBoolean("booked")
                )
            }
    }

    override fun getBookById(id: Int): Book? {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM book WHERE id = :id LIMIT 1", mapOf("id" to id), { rs, _ ->
                Book(
                    id = rs.getInt("id"),
                    title = rs.getString("title"),
                    author = rs.getString("author"),
                    booked = rs.getBoolean("booked")
                )
            }).firstOrNull()
    }

    override fun addBook(book: Book) {
        namedParameterJdbcTemplate
            .update(
                "INSERT INTO book (title, author, booked) values (:title, :author, :booked)", mapOf(
                    "title" to book.title,
                    "author" to book.author,
                    "booked" to book.booked
                )
            )
    }

    override fun setBookedStateOfBook(book: Book, booked: Boolean) {
        if (book.booked == booked) {
            throw IllegalArgumentException("Book is already $booked")
        }

        namedParameterJdbcTemplate
            .update(
                "UPDATE book SET booked = :booked WHERE id = :id",
                mapOf(
                    "id" to book.id,
                    "booked" to booked
                )
            )
    }
}