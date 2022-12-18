package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */

data class BidDTO(
    var registrationId: Long?,
    var roomId: Long,
    var categoryName: String,
    var checkIn: String,
    var checkOut: String,
    var isAccepted: Boolean?,
    var comment: String?,
    var paymentId: Long,
    var servicesNames: List<String>?,
    var totalPrice: Double
)