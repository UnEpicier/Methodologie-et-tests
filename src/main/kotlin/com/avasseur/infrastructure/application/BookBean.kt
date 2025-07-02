package com.avasseur.infrastructure.application

import com.avasseur.domain.usecase.BookListUseCase
import com.avasseur.infrastructure.driven.postgres.BookDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {
    @Bean
    fun bookListUseCase(bookDAO: BookDAO): BookListUseCase {
        return BookListUseCase(bookDAO)
    }
}