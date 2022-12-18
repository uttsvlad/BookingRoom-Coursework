package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import ru.coursework.courseworkpostgreskotlin.dto.UserDTO
import ru.coursework.courseworkpostgreskotlin.model.Administrator
import ru.coursework.courseworkpostgreskotlin.model.Client
import ru.coursework.courseworkpostgreskotlin.model.User
import ru.coursework.courseworkpostgreskotlin.repository.UserRepository
import ru.coursework.courseworkpostgreskotlin.util.errors.ErrorsReturner.Companion.returnTextOfErrors
import ru.coursework.courseworkpostgreskotlin.util.errors.user.UserNotFoundException
import java.util.*

/**
 * @author Vlad Utts
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val environment: Environment
) {
    fun saveAdmin(userDTO: UserDTO, administrator: Administrator) {
        val admin = User(
            username = userDTO.username,
            password = passwordEncoder.encode(userDTO.password),
            role = environment.getProperty("user.role.administrator")!!,
            registrationDate = Date(),
            client = null,
            administrator = administrator
        )
        userRepository.save(admin)
    }

    fun saveUser(userDTO: UserDTO, client: Client) {
        val user = User(
            username = userDTO.username,
            password = userDTO.password,
            role = environment.getProperty("user.role.client")!!,
            registrationDate = Date(),
            client = client,
            administrator = null
        )
        userRepository.save(user)
    }

    fun findExistUserByUsername(username: String) = userRepository.findById(username).get()

    fun findIfExistsOrThrow(username: String, bindingResult: BindingResult): User {
        val optionalUser = userRepository.findById(username)
        if (optionalUser.isEmpty) {
            bindingResult
                .addError(FieldError("user", "username", "User with this username not found!"))
            throw UserNotFoundException(returnTextOfErrors(bindingResult))
        }
        return optionalUser.get()
    }
}