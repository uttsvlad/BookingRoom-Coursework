package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class RoomForListDTO(
    var id: Long? = null,
    var categoryName: String = "",
    var price: Double = 0.0,
    var capacity: Int = 0
)