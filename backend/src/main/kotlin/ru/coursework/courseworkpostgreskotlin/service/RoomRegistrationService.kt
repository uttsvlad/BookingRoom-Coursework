package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.coursework.courseworkpostgreskotlin.dto.*
import ru.coursework.courseworkpostgreskotlin.model.Registration
import ru.coursework.courseworkpostgreskotlin.repository.RegistrationRepository
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


/**
 * @author Vlad Utts
 */
@Service
class RoomRegistrationService(
    private val registrationRepository: RegistrationRepository,
    private val roomService: RoomService,
    private val userService: UserService,
    private val paymentService: PaymentService,
    private val serviceService: ServiceService,
) {

    fun handleRoomRegistration(request: RoomRegistrationRequest) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val checkInDate = sdf.parse(request.checkIn)
        val checkOutDate = sdf.parse(request.checkOut)
        val room = roomService.findOne(request.roomId)
        val price = room!!.price
        val diffInMillies: Long = abs(checkOutDate.time - checkInDate.time)
        val days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
        var totalPrice = price * days
        var servicesPrice = 0.0
        val client = userService.findExistUserByUsername(request.username).client!!
        val payment = paymentService.find(request.paymentId)
        val services = mutableListOf<ru.coursework.courseworkpostgreskotlin.model.Service>()
        if (request.servicesIds != null) {
            for (i in request.servicesIds) {
                services.add(serviceService.findOne(i))
            }
        }
        for (service in services) {
            servicesPrice += service.servicePrice
        }
        servicesPrice *= days
        totalPrice += servicesPrice
        val registration = Registration(
            id = null,
            room = room,
            checkIn = checkInDate,
            checkOut = checkOutDate,
            totalPrice = totalPrice,
            administrator = null,
            isAccepted = null,
            comment = null,
            client = client,
            payment = payment,
            services = services
        )
        val reg = registrationRepository.save(registration)
        try {
            if (reg.room.category.name == "Люкс") registrationRepository.addServicesToLux(reg.id!!)
        } catch (e: RuntimeException) {
            return
        }
    }

    fun formBidList(username: String): MutableList<BidDTO> {
        val client = userService.findExistUserByUsername(username).client!!
        val registrations = registrationRepository.findAllByClient(client)
        val bids = mutableListOf<BidDTO>()
        for (registration in registrations) {
            bids.add(formBid(registration))
        }

        return bids
    }

    private fun formBid(registration: Registration): BidDTO {
        val servicesNames = mutableListOf<String>()
        if (registration.services != null) {
            for (service in registration.services) {
                servicesNames.add(service.serviceName)
            }
        }
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")
        return BidDTO(
            registration.id,
            registration.room.id,
            registration.room.category.name,
            dateFormatter.format(registration.checkIn),
            dateFormatter.format(registration.checkOut),
            registration.isAccepted,
            registration.comment,
            paymentId = registration.payment.id,
            servicesNames = servicesNames,
            totalPrice = registration.totalPrice
        )
    }

    fun formAdminBidList(): MutableList<AdminBidDTO> {
        val registrations = registrationRepository.findAll()
        val bids = mutableListOf<AdminBidDTO>()
        for (registration in registrations) {
            bids.add(formAdminBid(registration))
        }

        return bids
    }

    private fun formAdminBid(registration: Registration): AdminBidDTO {
        val client = registration.client
        val clientFullName = "${client.surname} ${client.firstName} ${client.middleName}"
        val servicesNames = mutableListOf<String>()
        if (registration.services != null) {
            for (service in registration.services) {
                servicesNames.add(service.serviceName)
            }
        }
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy")
        return AdminBidDTO(
            registration.id,
            registration.room.id,
            registration.room.category.name,
            dateFormatter.format(registration.checkIn),
            dateFormatter.format(registration.checkOut),
            registration.isAccepted,
            paymentId = registration.payment.id,
            servicesNames = servicesNames,
            totalPrice = registration.totalPrice,
            clientFullName = clientFullName
        )
    }

    @Transactional(readOnly = false)
    fun handleBid(request: AdminBidHandleRequest) {
        val id: Long = request.registrationId
        val registration = registrationRepository.findById(id).get()
        registration.administrator = userService.findExistUserByUsername(request.adminUsername).administrator
        registration.comment = request.comment
        registration.isAccepted = request.isAccepted

        registrationRepository.save(registration)
    }

    fun addDamage(request: AddDamageToRegistrationRequest) {
        try {
            registrationRepository.addDamage(request.registrationId, request.damageSum)
        } catch (e: RuntimeException) {
            return
        }
    }

}