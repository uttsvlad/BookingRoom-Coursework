package ru.coursework.courseworkpostgreskotlin.util.errors

import org.springframework.http.HttpStatus

/**
 * @author Vlad Utts
 */
data class ErrorsResponse(
    val status: HttpStatus,
    val errors: String?
)