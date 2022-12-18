package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
@Entity
data class Service(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    var id: Long,
    @field:NotEmpty(message = "Service name shouldn't be empty!")
    @field:Size(min = 3, max = 50, message = "Service name size should be between 3 and 50 characters!")
    val serviceName: String,
    @field:NotNull(message = "Service price shouldn't be null!")
    val servicePrice: Double,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Service

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , serviceName = $serviceName , servicePrice = $servicePrice )"
    }
}