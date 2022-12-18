package ru.coursework.courseworkpostgreskotlin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ru.coursework.courseworkpostgreskotlin.dto.AdministratorCreatingDTO
import ru.coursework.courseworkpostgreskotlin.dto.CategoryDTO
import ru.coursework.courseworkpostgreskotlin.dto.StatisticRequest
import ru.coursework.courseworkpostgreskotlin.dto.StatisticResponse
import ru.coursework.courseworkpostgreskotlin.model.Administrator
import ru.coursework.courseworkpostgreskotlin.service.AdministratorService
import ru.coursework.courseworkpostgreskotlin.service.CategoryService
import ru.coursework.courseworkpostgreskotlin.service.UserService
import ru.coursework.courseworkpostgreskotlin.util.DTOConverter
import ru.coursework.courseworkpostgreskotlin.util.errors.ErrorsResponse
import ru.coursework.courseworkpostgreskotlin.util.errors.ErrorsReturner.Companion.returnErrors
import ru.coursework.courseworkpostgreskotlin.util.errors.MyException
import ru.coursework.courseworkpostgreskotlin.util.errors.administrator.AdministratorNotCreatedException
import ru.coursework.courseworkpostgreskotlin.util.errors.category.CategoryNotCreatedException
import ru.coursework.courseworkpostgreskotlin.util.errors.category.CategoryValidator
import javax.validation.Valid

/**
 * @author Vlad Utts
 */
@RestController
@RequestMapping("/superAdmin")
class SuperAdminController(
    private val administratorService: AdministratorService,
    private val userService: UserService,
    private val categoryService: CategoryService,
    private val categoryValidator: CategoryValidator,
    private val dtoConverter: DTOConverter
) {
    @PostMapping("/createAdministrator")
    fun createAdministrator(
        @RequestBody @Valid administratorCreatingDTO: AdministratorCreatingDTO,
        bindingResult: BindingResult
    ): ResponseEntity<HttpStatus> {
        returnErrorsIfContains(bindingResult, AdministratorNotCreatedException())
        val administrator =
            administratorService.save(dtoConverter.convertToAdministrator(administratorCreatingDTO.administratorDTO))
        userService.saveAdmin(administratorCreatingDTO.userDTO, administrator)

        return ResponseEntity.ok(HttpStatus.OK)
    }

    @PostMapping("/addCategory")
    fun addCategory(
        @RequestBody @Valid categoryDTO: CategoryDTO,
        bindingResult: BindingResult
    ): ResponseEntity<HttpStatus> {
        categoryValidator.validate(categoryDTO, bindingResult)
        returnErrorsIfContains(bindingResult, CategoryNotCreatedException())

        categoryService.save(dtoConverter.convertToCategory(categoryDTO))

        return ResponseEntity.ok(HttpStatus.OK)
    }

    @GetMapping("/getAllAdmins")
    fun getAllAdmins(): List<Administrator> = administratorService.getAll()

    @PostMapping("/showStatistic")
    fun showStatistic(@RequestBody request: StatisticRequest): ResponseEntity<StatisticResponse> {

        administratorService.saveStatistic(request.administratorId, request.start, request.end)
        return ResponseEntity.ok(administratorService.formStatistic())
    }

    private fun returnErrorsIfContains(bindingResult: BindingResult, e: MyException) {
        if (bindingResult.hasErrors()) {
            returnErrors(bindingResult, e)
        }
    }

    @ExceptionHandler(AdministratorNotCreatedException::class, CategoryNotCreatedException::class)
    private fun handleException(e: MyException): ResponseEntity<ErrorsResponse> {
        val response = ErrorsResponse(
            status = HttpStatus.BAD_REQUEST,
            errors = e.message
        )
        return ResponseEntity<ErrorsResponse>(response, HttpStatus.BAD_REQUEST)
    }
}