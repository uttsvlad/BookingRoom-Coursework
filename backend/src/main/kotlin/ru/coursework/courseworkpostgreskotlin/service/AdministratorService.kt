package ru.coursework.courseworkpostgreskotlin.service

import org.springframework.stereotype.Service
import ru.coursework.courseworkpostgreskotlin.dto.StatisticResponse
import ru.coursework.courseworkpostgreskotlin.model.Administrator
import ru.coursework.courseworkpostgreskotlin.repository.AdministratorRepository
import java.io.BufferedReader
import java.io.FileReader
import java.util.*

/**
 * @author Vlad Utts
 */
@Service
class AdministratorService(private val administratorRepository: AdministratorRepository) {
    fun save(administrator: Administrator) = administratorRepository.save(administrator)
    fun saveStatistic(id: Long, start: Date, end: Date) {
        try {
            administratorRepository.saveStatistic(
                id,
                start,
                end,
                "C:\\Users\\uttsv\\IdeaProjects\\CourseWorkPostgresKotlin\\target\\classes\\static"
            )

        } catch (e: RuntimeException) {
            return
        }
    }

    fun formStatistic(): StatisticResponse {
        val records = mutableListOf<List<String>>()
        val br =
            BufferedReader(FileReader("C:\\Users\\uttsv\\IdeaProjects\\CourseWorkPostgresKotlin\\target\\classes\\static\\admin_statistics.csv"))
        var line: String?

        while (true) {
            line = br.readLine()
            if (line == null)
                break
            val values = line.split(",")
            records.add(values)
        }
        return StatisticResponse(records[1][0].toInt(), records[1][1].toInt(), records[1][2].toDouble())
    }

    fun getAll(): List<Administrator> = administratorRepository.findAll()
}