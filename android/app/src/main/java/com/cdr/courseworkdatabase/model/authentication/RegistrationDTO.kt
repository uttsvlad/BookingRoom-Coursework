package com.cdr.courseworkdatabase.model.authentication

data class RegistrationDTO(
    val userDTO: UserDTO,
    val clientDTO: ClientDTO
)