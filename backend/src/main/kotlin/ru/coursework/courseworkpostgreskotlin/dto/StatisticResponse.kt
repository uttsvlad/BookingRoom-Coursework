package ru.coursework.courseworkpostgreskotlin.dto

/**
 * @author Vlad Utts
 */
data class StatisticResponse(
    val allCheckinsCount: Int,
    val luxCount: Int,
    val totalRevenue: Double
)