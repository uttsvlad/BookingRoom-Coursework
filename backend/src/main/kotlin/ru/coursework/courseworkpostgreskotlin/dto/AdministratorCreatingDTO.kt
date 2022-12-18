package ru.coursework.courseworkpostgreskotlin.dto

import javax.validation.Valid
import javax.validation.constraints.NotNull


/**
 * @author Vlad Utts
 */
data class AdministratorCreatingDTO(
    @field:Valid
    @field:NotNull(message = "AdministratorDTO shouldn't be null!")
    val administratorDTO: AdministratorDTO,
    @field:Valid
    @field:NotNull(message = "UserDTO shouldn't be null!")
    val userDTO: UserDTO
)