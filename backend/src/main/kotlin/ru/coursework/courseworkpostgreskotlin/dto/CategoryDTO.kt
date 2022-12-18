package ru.coursework.courseworkpostgreskotlin.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
data class CategoryDTO(
    @field:NotEmpty(message = "Category name shouldn't be empty!")
    @field:Size(min = 3, max = 50, message = "Category name size should be between 3 and 50 characters!")
    var name: String = ""
)