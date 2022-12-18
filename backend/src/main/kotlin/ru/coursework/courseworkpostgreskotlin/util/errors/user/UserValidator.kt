package ru.coursework.courseworkpostgreskotlin.util.errors.user

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import ru.coursework.courseworkpostgreskotlin.dto.UserDTO
import ru.coursework.courseworkpostgreskotlin.service.UserDetailsService


/**
 * @author Vlad Utts
 */
@Component
class UserValidator(
    private val userDetailsService: UserDetailsService
) : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return UserDTO::class.java == clazz
    }

    override fun validate(target: Any, errors: Errors) {
        val userDTO: UserDTO = target as UserDTO

        try {
            userDetailsService.loadUserByUsername(userDTO.username)
        } catch (ignored: UsernameNotFoundException) {
            return  // все ок, пользователь не найден
        }

        errors.rejectValue("userDTO.username", "", "User with this username already exists!")
    }
}