package ru.coursework.courseworkpostgreskotlin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ru.coursework.courseworkpostgreskotlin.dto.BidDTO
import ru.coursework.courseworkpostgreskotlin.dto.ComplaintRequestDTO
import ru.coursework.courseworkpostgreskotlin.dto.RoomRegistrationRequest
import ru.coursework.courseworkpostgreskotlin.dto.RoomForListDTO
import ru.coursework.courseworkpostgreskotlin.service.*
import ru.coursework.courseworkpostgreskotlin.util.DTOConverter
import ru.coursework.courseworkpostgreskotlin.util.errors.ErrorsResponse
import ru.coursework.courseworkpostgreskotlin.util.errors.ErrorsReturner.Companion.returnErrors
import ru.coursework.courseworkpostgreskotlin.util.errors.MyException
import ru.coursework.courseworkpostgreskotlin.util.errors.complaint.ComplaintNotCreatedException
import ru.coursework.courseworkpostgreskotlin.util.errors.complaint.ComplaintValidator
import javax.validation.Valid


/**
 * @author Vlad Utts
 */
@RestController
@RequestMapping("/client")
class ClientController(
    private val clientService: ClientService,
    private val userService: UserService,
    private val complaintService: ComplaintService,
    private val roomService: RoomService,
    private val complaintValidator: ComplaintValidator,
    private val serviceService: ServiceService,
    private val registrationService: RoomRegistrationService,
    private val dtoConverter: DTOConverter

) {
    @PostMapping("/createComplaint")
    fun createComplaint(
        @RequestBody @Valid complaintDTO: ComplaintRequestDTO,
        bindingResult: BindingResult
    ): ResponseEntity<HttpStatus> {
        complaintValidator.validate(complaintDTO, bindingResult)
        returnErrorsIfContains(bindingResult, ComplaintNotCreatedException())

        val username = SecurityContextHolder.getContext().authentication.name
        val user = userService.findExistUserByUsername(username)
        val complaint = user.client?.let { dtoConverter.convertToComplaint(complaintDTO, it) }
        complaint?.let { complaintService.saveComplaint(it) }

        return ResponseEntity.ok(HttpStatus.OK)
    }

    @GetMapping("/showRoomInfo/{id}")
    fun showRoomInfo(@PathVariable id: Long) =
        roomService.findOne(id)?.let { dtoConverter.convertToRoomDTO(it, serviceService.findAll()) }

    @GetMapping("/showAllRooms")
    fun showAllRooms(): List<RoomForListDTO> {
        val rooms = roomService.findAll()
        val roomDTOs = mutableListOf<RoomForListDTO>()
        for (room in rooms) {
            roomDTOs.add(dtoConverter.convertToRoomForListDTO(room))
        }

        return roomDTOs
    }

    @PostMapping("/sendRoomRegistration")
    fun sendRoomRegistration(@RequestBody roomRegistrationRequest: RoomRegistrationRequest): ResponseEntity<HttpStatus> {
        registrationService.handleRoomRegistration(roomRegistrationRequest)

        return ResponseEntity.ok(HttpStatus.OK)
    }

    @GetMapping("/{username}/getBids")
    fun getBids(@PathVariable username: String): MutableList<BidDTO> {
         return registrationService.formBidList(username)
    }

    private fun returnErrorsIfContains(bindingResult: BindingResult, e: MyException) {
        if (bindingResult.hasErrors()) {
            returnErrors(bindingResult, e)
        }
    }

    @ExceptionHandler(ComplaintNotCreatedException::class)
    private fun handleException(e: MyException): ResponseEntity<ErrorsResponse> {
        val response = ErrorsResponse(
            status = HttpStatus.BAD_REQUEST,
            errors = e.message
        )
        return ResponseEntity<ErrorsResponse>(response, HttpStatus.BAD_REQUEST)
    }
}