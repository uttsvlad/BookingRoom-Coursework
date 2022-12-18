package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.User

/**
 * @author Vlad Utts
 */
@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByClientId(clientId: Long): User
}