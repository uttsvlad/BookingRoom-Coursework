package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class AdminBidDTO(
    var registrationId: Long?,
    var roomId: Long,
    var categoryName: String,
    var checkIn: String,
    var checkOut: String,
    var isAccepted: Boolean?,
    var paymentId: Long,
    var servicesNames: List<String>?,
    var totalPrice: Double,
    var clientFullName: String
)