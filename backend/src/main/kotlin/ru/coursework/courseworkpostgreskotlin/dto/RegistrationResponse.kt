package ru.coursework.courseworkpostgreskotlin.dto

import org.springframework.http.HttpStatus

/**
 * @author Vlad Utts
 */
data class RegistrationResponse(
    val status: HttpStatus,
    val errors: String?
)