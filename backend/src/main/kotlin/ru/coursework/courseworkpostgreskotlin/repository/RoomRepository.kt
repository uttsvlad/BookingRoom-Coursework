package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.Room

/**
 * @author Vlad Utts
 */
@Repository
interface RoomRepository : JpaRepository<Room, Long> {
}