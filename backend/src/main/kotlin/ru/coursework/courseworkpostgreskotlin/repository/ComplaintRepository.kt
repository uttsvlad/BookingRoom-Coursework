package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.Complaint
import java.util.*

/**
 * @author Vlad Utts
 */
@Repository
interface ComplaintRepository : JpaRepository<Complaint, Long> {
    fun findByContent(complaintContent: String): Optional<Complaint>
}