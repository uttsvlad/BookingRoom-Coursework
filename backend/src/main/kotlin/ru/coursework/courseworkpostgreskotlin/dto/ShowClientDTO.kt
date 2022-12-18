package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class ShowClientDTO(
    val clientDTO: ClientDTO,
    val username: String,
    val registrationDate: String
)