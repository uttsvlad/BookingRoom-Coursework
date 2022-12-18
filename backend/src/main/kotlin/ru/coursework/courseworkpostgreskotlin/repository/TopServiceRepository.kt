package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.TopService

/**
 * @author Vlad Utts
 */
@Repository
interface TopServiceRepository : JpaRepository<TopService, Long>