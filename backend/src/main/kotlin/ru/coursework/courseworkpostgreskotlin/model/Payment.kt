package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
@Entity
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    val id: Long,
    @field:NotEmpty(message = "Payment method shouldn't be empty!")
    @field:Size(min = 5, max = 8, message = "Payment method size should be between 5 and 8 characters!")
    val paymentMethod: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Payment

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , paymentMethod = $paymentMethod )"
    }
}