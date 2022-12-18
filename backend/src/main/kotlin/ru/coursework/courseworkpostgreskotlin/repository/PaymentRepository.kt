package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.Payment

/**
 * @author Vlad Utts
 */
@Repository
interface PaymentRepository : JpaRepository<Payment, Long> {
}