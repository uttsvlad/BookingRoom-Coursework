package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.Category
import java.util.*

/**
 * @author Vlad Utts
 */
@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Optional<Category>
}