package ru.coursework.courseworkpostgreskotlin.dto

import java.util.Date

/**
 * @author Vlad Utts
 */
data class StatisticRequest(
    val administratorId: Long,
    val start: Date,
    val end: Date
)