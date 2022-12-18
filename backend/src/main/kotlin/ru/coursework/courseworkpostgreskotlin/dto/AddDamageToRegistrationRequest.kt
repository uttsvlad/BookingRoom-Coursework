package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class AddDamageToRegistrationRequest(
    val registrationId: Long,
    val damageSum: Int
)