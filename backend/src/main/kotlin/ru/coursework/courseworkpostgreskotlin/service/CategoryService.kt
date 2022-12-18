package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import ru.coursework.courseworkpostgreskotlin.model.Category
import ru.coursework.courseworkpostgreskotlin.repository.CategoryRepository
import ru.coursework.courseworkpostgreskotlin.util.errors.category.CategoryNotFoundException
import ru.coursework.courseworkpostgreskotlin.util.errors.complaint.ComplaintNotFoundException

/**
 * @author Vlad Utts
 */
@Service
class CategoryService(private val categoryRepository: CategoryRepository) {
    fun save(category: Category) = categoryRepository.save(category)

    fun findIfExistsOrThrow(categoryName: String) {
        val categoryOptional = categoryRepository.findByName(categoryName)
        if (categoryOptional.isEmpty)
            throw CategoryNotFoundException()
    }
}