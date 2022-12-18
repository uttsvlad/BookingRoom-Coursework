package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import ru.coursework.courseworkpostgreskotlin.model.Complaint
import ru.coursework.courseworkpostgreskotlin.repository.ComplaintRepository
import ru.coursework.courseworkpostgreskotlin.util.errors.complaint.ComplaintNotFoundException

/**
 * @author Vlad Utts
 */
@Service
class ComplaintService(
    private val complaintRepository: ComplaintRepository,
    private val userService: UserService
) {
    fun showAllComplaints(): List<Complaint> = complaintRepository.findAll()
    fun saveComplaint(complaint: Complaint) = complaintRepository.save(complaint)

    fun findIfExistsOrThrow(complaintContent: String) {
        val complaintOptional = complaintRepository.findByContent(complaintContent)
        if (complaintOptional.isEmpty)
            throw ComplaintNotFoundException()
    }
}