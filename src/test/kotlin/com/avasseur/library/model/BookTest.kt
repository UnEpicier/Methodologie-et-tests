package com.avasseur.library.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class BookTest : StringSpec({
    "A book needs an author" {
        shouldThrow<IllegalArgumentException> { Book( "", "Title") }
    }

    "A book needs a title" {
        shouldThrow<IllegalArgumentException> { Book( "Author", "") }
    }
})