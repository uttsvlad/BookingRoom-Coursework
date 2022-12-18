package ru.coursework.courseworkpostgreskotlin.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.coursework.courseworkpostgreskotlin.dto.AddDamageToRegistrationRequest
import ru.coursework.courseworkpostgreskotlin.dto.AdminBidHandleRequest
import ru.coursework.courseworkpostgreskotlin.dto.TopServiceDTO
import ru.coursework.courseworkpostgreskotlin.service.*
import ru.coursework.courseworkpostgreskotlin.util.DTOConverter
import java.util.*


/**
 * @author Vlad Utts
 */
@RestController
@RequestMapping("/administrator")
class AdministratorController(
    private val complaintService: ComplaintService,
    private val clientService: ClientService,
    private val roomRegistrationService: RoomRegistrationService,
    private val serviceService: ServiceService,
    private val dtoConverter: DTOConverter
) {

    @GetMapping("/showComplaints")
    fun showComplaints() = complaintService.showAllComplaints().map { dtoConverter.convertToComplaintDTO(it) }

    @GetMapping("/showClients")
    fun showClients() = clientService.getAllShowClientDTOs()

    @GetMapping("/showBids")
    fun showBids() = roomRegistrationService.formAdminBidList()

    @PostMapping("/handleBid")
    fun handleBid(@RequestBody request: AdminBidHandleRequest): ResponseEntity<HttpStatus> {
        roomRegistrationService.handleBid(request)

        return ResponseEntity.ok(HttpStatus.OK)
    }

    @PostMapping("/addDamage")
    fun addDamage(@RequestBody request: AddDamageToRegistrationRequest): ResponseEntity<HttpStatus> {
        roomRegistrationService.addDamage(request)

        return ResponseEntity.ok(HttpStatus.OK)
    }

    @GetMapping("/topService")
    fun freeRooms(): ResponseEntity<TopServiceDTO> {
        return ResponseEntity.ok(serviceService.topService())
    }
}