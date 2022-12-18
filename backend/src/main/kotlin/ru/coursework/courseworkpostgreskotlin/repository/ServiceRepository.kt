package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.Service

/**
 * @author Vlad Utts
 */
@Repository
interface ServiceRepository : JpaRepository<Service, Long> {
}