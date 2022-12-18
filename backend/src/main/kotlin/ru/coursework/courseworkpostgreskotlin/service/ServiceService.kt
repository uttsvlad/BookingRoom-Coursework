package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import ru.coursework.courseworkpostgreskotlin.dto.TopServiceDTO
import ru.coursework.courseworkpostgreskotlin.repository.ServiceRepository
import ru.coursework.courseworkpostgreskotlin.repository.TopServiceRepository

/**
 * @author Vlad Utts
 */
@Service
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val topServiceRepository: TopServiceRepository
) {
    fun findAll(): List<ru.coursework.courseworkpostgreskotlin.model.Service> = serviceRepository.findAll()
    fun findOne(id: Long): ru.coursework.courseworkpostgreskotlin.model.Service = serviceRepository.findById(id).get()
    fun topService(): TopServiceDTO {
        val topService = topServiceRepository.findAll()[0]
        val service = findOne(topService.serviceId)
        return TopServiceDTO(service.serviceName, service.servicePrice, topService.count)
    }
}