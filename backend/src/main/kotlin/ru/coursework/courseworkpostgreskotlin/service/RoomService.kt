package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import ru.coursework.courseworkpostgreskotlin.dto.RoomDTO
import ru.coursework.courseworkpostgreskotlin.model.Room
import ru.coursework.courseworkpostgreskotlin.repository.RoomRepository
import java.util.*

/**
 * @author Vlad Utts
 */
@Service
class RoomService(
    private val roomRepository: RoomRepository
) {
    fun findAll(): List<Room> = roomRepository.findAll()

    fun findOne(id: Long): Room? {
        val roomOptional = roomRepository.findById(id)
        if (roomOptional.isEmpty)
            return null
        return roomOptional.get()
    }
}