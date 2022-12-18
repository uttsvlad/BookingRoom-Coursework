package ru.coursework.courseworkpostgreskotlin.util.errors.category

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import ru.coursework.courseworkpostgreskotlin.dto.CategoryDTO
import ru.coursework.courseworkpostgreskotlin.dto.ComplaintRequestDTO
import ru.coursework.courseworkpostgreskotlin.service.CategoryService
import ru.coursework.courseworkpostgreskotlin.util.errors.complaint.ComplaintNotFoundException

/**
 * @author Vlad Utts
 */
@Component
class CategoryValidator(private val categoryService: CategoryService) : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return CategoryDTO::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {
        val categoryDTO: CategoryDTO = target as CategoryDTO

        try {
            categoryService.findIfExistsOrThrow(categoryDTO.name)
        } catch (ignored: CategoryNotFoundException) {
            return  // все ок, пользователь не найден
        }

        errors.rejectValue("name", "", "This category already exists!")
    }
}