package com.avasseur.library.model

data class Book(val name: String, val author: String) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(author.isNotBlank()) { "Author must not be blank" }
    }
}