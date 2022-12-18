package ru.coursework.courseworkpostgreskotlin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.coursework.courseworkpostgreskotlin.dto.*
import ru.coursework.courseworkpostgreskotlin.security.JWTUtil
import ru.coursework.courseworkpostgreskotlin.service.ClientService
import ru.coursework.courseworkpostgreskotlin.service.UserService
import ru.coursework.courseworkpostgreskotlin.util.DTOConverter
import ru.coursework.courseworkpostgreskotlin.util.errors.ErrorsResponse
import ru.coursework.courseworkpostgreskotlin.util.errors.ErrorsReturner.Companion.returnErrors
import ru.coursework.courseworkpostgreskotlin.util.errors.MyException
import ru.coursework.courseworkpostgreskotlin.util.errors.user.UserNotCreatedException
import ru.coursework.courseworkpostgreskotlin.util.errors.user.UserNotFoundException
import ru.coursework.courseworkpostgreskotlin.util.errors.user.UserValidator
import java.text.SimpleDateFormat
import javax.validation.Valid

/**
 * @author Vlad Utts
 */
@RestController
class AuthenticationController(
    private val userService: UserService,
    private val clientService: ClientService,
    private val userValidator: UserValidator,
    private val dtoConverter: DTOConverter,
    private val jwtUtil: JWTUtil,
    private val authenticationManager: AuthenticationManager
) {
    @PostMapping("/registration")
    fun registration(
        @RequestBody @Valid registrationDTO: RegistrationDTO,
        bindingResult: BindingResult
    ): RegistrationResponse {
        val userDTO = registrationDTO.userDTO
        userValidator.validate(userDTO, bindingResult)
        returnErrorsIfContains(bindingResult, UserNotCreatedException())

        val clientDTO = registrationDTO.clientDTO
        val client = dtoConverter.convertToClient(clientDTO)
        clientService.save(client)
        userService.saveUser(userDTO, client)

        return RegistrationResponse(
            status = HttpStatus.OK,
            errors = null
        )
    }

    @PostMapping("/login")
    fun performLogin(@RequestBody @Valid userDTO: UserDTO, bindingResult: BindingResult): AuthorizationResponse {
        returnErrorsIfContains(bindingResult, UserNotFoundException())
        val user = userService.findIfExistsOrThrow(userDTO.username, bindingResult)

        val authToken = UsernamePasswordAuthenticationToken(userDTO.username, userDTO.password)
        authenticationManager.authenticate(authToken)
        val token = jwtUtil.generateToken(userDTO.username)
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")
        val formattedRegistrationDate = dateFormatter.format(user.registrationDate)
        if (user.client != null && user.role == "ROLE_CLIENT")
            return AuthorizationResponse(
                status = HttpStatus.OK,
                jwtToken = token,
                registrationDate = formattedRegistrationDate,
                role = "Клиент",
                clientDTO = dtoConverter.convertToClientDTO(user.client),
                administratorDTO = null
            )
        else if (user.administrator != null && user.role == "ROLE_ADMINISTRATOR")
            return AuthorizationResponse(
                status = HttpStatus.OK,
                jwtToken = token,
                registrationDate = formattedRegistrationDate,
                role = "Администратор",
                clientDTO = null,
                administratorDTO = dtoConverter.convertToAdministratorDTO(user.administrator)
            )
        else if (user.role == "ROLE_SUPER_ADMIN")
            return AuthorizationResponse(
                status = HttpStatus.OK,
                jwtToken = token,
                registrationDate = formattedRegistrationDate,
                role = "Суперадмин",
                clientDTO = null,
                administratorDTO = null
            )
        else
            throw UserNotFoundException()
    }

    private fun returnErrorsIfContains(bindingResult: BindingResult, e: MyException) {
        if (bindingResult.hasErrors()) {
            returnErrors(bindingResult, e)
        }
    }

    @ExceptionHandler(UserNotCreatedException::class, UserNotFoundException::class)
    private fun handleException(e: MyException): ResponseEntity<ErrorsResponse> {
        val response = ErrorsResponse(
            status = HttpStatus.BAD_REQUEST,
            errors = e.message
        )
        return ResponseEntity<ErrorsResponse>(response, HttpStatus.BAD_REQUEST)
    }
}