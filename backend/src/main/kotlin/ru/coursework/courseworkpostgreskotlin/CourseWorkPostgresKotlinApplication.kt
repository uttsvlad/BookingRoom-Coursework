package ru.coursework.courseworkpostgreskotlin

import org.modelmapper.ModelMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CourseWorkPostgresKotlinApplication{
    @Bean
    fun modelMapper() = ModelMapper()
}

fun main(args: Array<String>) {
    runApplication<CourseWorkPostgresKotlinApplication>(*args)
}

