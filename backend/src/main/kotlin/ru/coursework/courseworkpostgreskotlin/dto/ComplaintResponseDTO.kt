package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class ComplaintResponseDTO(
    var content: String = "",
    var clientSurname: String = "",
    var clientFirstName: String = "",
    var clientMiddleName: String? = null
)