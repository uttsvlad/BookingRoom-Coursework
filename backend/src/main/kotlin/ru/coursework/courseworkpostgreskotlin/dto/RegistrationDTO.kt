package ru.coursework.courseworkpostgreskotlin.dto

import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * @author Vlad Utts
 */
data class RegistrationDTO(
    @field:Valid
    @field:NotNull(message = "UserDTO shouldn't be null!")
    val userDTO: UserDTO,
    @field:Valid
    @field:NotNull(message = "ClientDTO shouldn't be null!")
    val clientDTO: ClientDTO
)