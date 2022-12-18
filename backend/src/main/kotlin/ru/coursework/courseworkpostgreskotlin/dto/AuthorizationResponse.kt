package ru.coursework.courseworkpostgreskotlin.dto

import org.springframework.http.HttpStatus
import java.util.*

/**
 * @author Vlad Utts
 */
data class AuthorizationResponse(
    val status: HttpStatus,
    val jwtToken: String,
    val registrationDate: String,
    val role: String,
    val clientDTO: ClientDTO?,
    val administratorDTO: AdministratorDTO?
)