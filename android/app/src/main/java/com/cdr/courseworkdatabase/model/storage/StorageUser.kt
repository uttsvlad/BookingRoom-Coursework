package com.cdr.courseworkdatabase.model.storage

data class StorageUser(
    val surname: String,
    val firstName: String,
    val middleName: String,
    val role: String,
    val username: String,
    val documentName: String,
    val documentID: String,
    val dateOfCreation: String,
    val jwtToken: String
)
