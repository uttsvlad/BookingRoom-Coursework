package ru.coursework.courseworkpostgreskotlin.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
data class UserDTO(
    @field:NotEmpty(message = "Username shouldn't be empty!")
    @field:Size(min = 3, max = 30, message = "Username size should be between 3 and 30 characters!")
    val username: String,
    @field:NotEmpty(message = "Password shouldn't be empty!")
    @field:Size(min = 8, max = 30, message = "Password size should be between 8 and 30 characters!")
    val password: String,
)