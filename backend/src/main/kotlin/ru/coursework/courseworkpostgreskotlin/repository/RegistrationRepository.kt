package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.Client
import ru.coursework.courseworkpostgreskotlin.model.Registration

/**
 * @author Vlad Utts
 */
@Repository
interface RegistrationRepository : JpaRepository<Registration, Long> {
    @Query(value = "call bigger_price(:reg_id, :damage_sum);", nativeQuery = true)
    fun addDamage(@Param("reg_id") registrationId: Long, @Param("damage_sum") damageSum: Int)

    @Query(value = "call add_services_to_lux_transaction(:reg_id);", nativeQuery = true)
    fun addServicesToLux(@Param("reg_id") registrationId: Long)

    fun findAllByClient(client: Client): List<Registration>
}