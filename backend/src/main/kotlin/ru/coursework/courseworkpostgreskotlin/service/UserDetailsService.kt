package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.coursework.courseworkpostgreskotlin.repository.UserRepository
import ru.coursework.courseworkpostgreskotlin.security.UserDetails

/**
 * @author Vlad Utts
 */
@Service
@Transactional(readOnly = true)
class UserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val optional = userRepository.findById(username)
        if (optional.isEmpty) throw UsernameNotFoundException("User not found!")

        return UserDetails(optional.get())
    }
}