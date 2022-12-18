package com.cdr.courseworkdatabase.model.authentication

data class AuthenticationResponse(
    val status: String,
    val jwtToken: String?,
    val registrationDate: String,
    val role: String,
    val clientDTO: ClientDTO?,
    val administratorDTO: AdministratorDTO?
)
