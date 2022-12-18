package com.cdr.courseworkdatabase.model.authentication

data class ClientDTO(
    val surname: String,
    val firstName: String,
    val middleName: String,
    val documentName: String,
    val documentNumber: String,
)