package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import ru.coursework.courseworkpostgreskotlin.repository.PaymentRepository

/**
 * @author Vlad Utts
 */
@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {
    fun find(id: Long) = paymentRepository.findById(id).get()
}