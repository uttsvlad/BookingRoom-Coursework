package ru.coursework.courseworkpostgreskotlin.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
data class AdministratorDTO(
    @field:NotEmpty(message = "Surname shouldn't be empty!")
    @field:Size(min = 1, max = 150, message = "Surname's size should be between 3 and 150 characters!")
    var surname: String = "",
    @field:NotEmpty(message = "Firstname shouldn't be empty!")
    @field:Size(min = 1, max = 150, message = "Firstname's size should be between 3 and 150 characters!")
    var firstName: String = "",
    var middleName: String? = null
)