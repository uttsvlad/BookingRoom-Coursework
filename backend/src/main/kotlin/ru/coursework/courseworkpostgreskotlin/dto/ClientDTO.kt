package ru.coursework.courseworkpostgreskotlin.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size


/**
 * @author Vlad Utts
 */

data class ClientDTO(
    @field:NotEmpty(message = "Surname shouldn't be empty!")
    @field:Size(min = 1, max = 150, message = "Surname's size should be between 1 and 150 characters!")
    var surname: String = "",
    @field:NotEmpty(message = "Firstname shouldn't be empty!")
    @field:Size(min = 1, max = 150, message = "Firstname's size should be between 1 and 150 characters!")
    var firstName: String = "",
    var middleName: String? = null,
    @field:NotEmpty(message = "Document name shouldn't be empty!")
    @field:Size(min = 7, max = 14, message = "Document name's size should be between 7 and 14 characters!")
    var documentName: String = "",
    @field:NotEmpty(message = "Document name shouldn't be empty!")
    @field:Size(min = 3, max = 50, message = "Document name's size should be between 3 and 50 characters!")
    var documentNumber: String = ""
)