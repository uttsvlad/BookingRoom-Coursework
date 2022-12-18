package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * @author Vlad Utts
 */
@Entity
@Immutable
data class TopService(
    @Id
    @Column(name = "service_id")
    val serviceId: Long,
    @Column(name = "count")
    val count: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as TopService

        return serviceId == other.serviceId
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String = this::class.simpleName + "(serviceId = $serviceId , count = $count )"
}