package ru.coursework.courseworkpostgreskotlin.dto


/**
 * @author Vlad Utts
 */

data class RoomRegistrationRequest(
    val username: String,
    val roomId: Long,
    val checkIn: String,
    val checkOut: String,
    val servicesIds: List<Long>?,
    val paymentId: Long
)