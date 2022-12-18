package ru.coursework.courseworkpostgreskotlin.util.errors.complaint

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import ru.coursework.courseworkpostgreskotlin.dto.ComplaintRequestDTO
import ru.coursework.courseworkpostgreskotlin.service.ComplaintService

/**
 * @author Vlad Utts
 */

@Component
class ComplaintValidator(private val complaintService: ComplaintService) : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return ComplaintRequestDTO::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {
        val complaintRequestDTO: ComplaintRequestDTO = target as ComplaintRequestDTO

        try {
            complaintService.findIfExistsOrThrow(complaintRequestDTO.content)
        } catch (ignored: ComplaintNotFoundException) {
            return  // все ок, пользователь не найден
        }

        errors.rejectValue("content", "", "This complaint already exists!")
    }
}