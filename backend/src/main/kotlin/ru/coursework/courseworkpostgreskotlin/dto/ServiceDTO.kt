package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class ServiceDTO(
    var id: Long = 0,
    var serviceName: String = "",
    var servicePrice: Double = 0.0
)