package ru.coursework.courseworkpostgreskotlin.util

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component
import ru.coursework.courseworkpostgreskotlin.dto.*
import ru.coursework.courseworkpostgreskotlin.model.*

/**
 * @author Vlad Utts
 */
@Component
class DTOConverter(private val modelMapper: ModelMapper) {
    fun convertToComplaint(complaintDTO: ComplaintRequestDTO, client: Client): Complaint {
        val complaint = modelMapper.map(complaintDTO, Complaint::class.java)
        complaint.client = client

        return complaint
    }

    fun convertToComplaintDTO(complaint: Complaint): ComplaintResponseDTO {
        val complaintDTO = modelMapper.map(complaint, ComplaintResponseDTO::class.java)
        val client = complaint.client
        complaintDTO.clientSurname = client.surname
        complaintDTO.clientFirstName = client.firstName
        complaintDTO.clientMiddleName = client.middleName

        return complaintDTO
    }

    fun convertToClient(clientDTO: ClientDTO): Client {
        return modelMapper.map(clientDTO, Client::class.java)
    }

    fun convertToClientDTO(client: Client): ClientDTO {
        return modelMapper.map(client, ClientDTO::class.java)
    }

    fun convertToAdministrator(administratorDTO: AdministratorDTO): Administrator {
        return modelMapper.map(administratorDTO, Administrator::class.java)
    }

    fun convertToAdministratorDTO(administrator: Administrator): AdministratorDTO {
        return modelMapper.map(administrator, AdministratorDTO::class.java)
    }

    fun convertToCategory(categoryDTO: CategoryDTO): Category {
        return modelMapper.map(categoryDTO, Category::class.java)
    }

    fun convertToCategoryDTO(category: Category): CategoryDTO {
        return modelMapper.map(category, CategoryDTO::class.java)
    }

    fun convertToRoomDTO(room: Room, services: List<Service>): RoomDTO {
        val roomDTO = modelMapper.map(room, RoomDTO::class.java)
        roomDTO.categoryName = room.category.name
        roomDTO.windowViewName = room.windowView.name
        val photosURLs = mutableListOf<String>()
        for (photo in room.photos!!)
            photosURLs.add(photo.URL)

        roomDTO.photos = photosURLs
        val serviceDTOs = mutableListOf<ServiceDTO>()
        for (service in services) {
            serviceDTOs.add(convertToServiceDTO(service))
        }
        roomDTO.services = serviceDTOs
        return roomDTO
    }

    fun convertToRoomForListDTO(room: Room): RoomForListDTO {
        val roomDTO = modelMapper.map(room, RoomForListDTO::class.java)
        roomDTO.categoryName = room.category.name
        return roomDTO
    }

    fun convertToServiceDTO(service: Service): ServiceDTO {
        return modelMapper.map(service, ServiceDTO::class.java)
    }

}