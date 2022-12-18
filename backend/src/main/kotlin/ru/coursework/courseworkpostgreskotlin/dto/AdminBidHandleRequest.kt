package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class AdminBidHandleRequest(
    val registrationId: Long,
    val adminUsername: String,
    val isAccepted: Boolean,
    val comment: String
)