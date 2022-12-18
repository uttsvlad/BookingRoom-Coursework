package ru.coursework.courseworkpostgreskotlin.dto

import javax.persistence.Column
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
data class ComplaintRequestDTO(
    @Column(name = "complaint_content")
    @field:NotEmpty(message = "Complaint's content shouldn't be empty!")
    @field:Size(min = 5, max = 300, message = "Complaint's content size should be between 5 and 300 characters!")
    var content: String = "",
)