package ru.coursework.courseworkpostgreskotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.coursework.courseworkpostgreskotlin.model.Administrator
import java.util.Date

/**
 * @author Vlad Utts
 */
@Repository
interface AdministratorRepository : JpaRepository<Administrator, Long> {
    @Query(value = "select save_admin_statistic_to_csv(:id, :start_period, :end_period, :filepath);", nativeQuery = true)
    fun saveStatistic(
        @Param("id") administratorId: Long, @Param("start_period") startPeriod: Date,
        @Param("end_period") endPeriod: Date, @Param("filepath") filePath: String
    )
}