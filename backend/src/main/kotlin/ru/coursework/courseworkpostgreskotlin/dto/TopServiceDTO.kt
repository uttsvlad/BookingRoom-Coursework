package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class TopServiceDTO(
    val serviceName: String,
    val servicePrice: Double,
    val count: Long
)