package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import ru.coursework.courseworkpostgreskotlin.dto.RoomRegistrationRequest
import ru.coursework.courseworkpostgreskotlin.dto.ShowClientDTO
import ru.coursework.courseworkpostgreskotlin.model.Client
import ru.coursework.courseworkpostgreskotlin.repository.ClientRepository
import ru.coursework.courseworkpostgreskotlin.repository.UserRepository
import ru.coursework.courseworkpostgreskotlin.util.DTOConverter
import java.text.SimpleDateFormat

/**
 * @author Vlad Utts
 */
@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val userRepository: UserRepository,
    private val dtoConverter: DTOConverter
) {
    fun save(client: Client) = clientRepository.save(client)

    fun get(id: Long) = clientRepository.findById(id)

    fun getAll(): List<Client> = clientRepository.findAll()

    private fun formShowClientDTO(client: Client): ShowClientDTO {
        val user = userRepository.findByClientId(client.id)
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy");
        return ShowClientDTO(
            clientDTO = dtoConverter.convertToClientDTO(client),
            username = user.username,
            registrationDate = dateFormatter.format(user.registrationDate)

        )
    }

    fun getAllShowClientDTOs() = getAll().map { formShowClientDTO(it) }
}