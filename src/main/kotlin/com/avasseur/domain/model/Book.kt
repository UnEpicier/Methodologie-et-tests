package com.avasseur.domain.model

data class Book(val id: Int?, val title: String, val author: String, val booked: Boolean = false) {
    init {
        require(title.isNotBlank()) { "Name must not be blank" }
        require(author.isNotBlank()) { "Author must not be blank" }
    }
    
    constructor(title: String, author: String, booked: Boolean = false) :
            this(null, title, author, booked)
}