package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class RoomDTO(
    var id: Long? = null,
    var categoryName: String = "",
    var windowViewName: String = "",
    var price: Double = 0.0,
    var capacity: Int = 0,
    var floor: Int = 0,
    var conditionerAvailable: Boolean = false,
    var hairDryerAvailable: Boolean = false,
    var description: String = "",
    var services: List<ServiceDTO>? = null,
    var photos: List<String>? = null
)