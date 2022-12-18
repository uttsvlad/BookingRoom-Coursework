package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author Vlad Utts
 */
@Entity
class Registration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    val id: Long?,
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "administrator_id", referencedColumnName = "administrator_id")
    var administrator: Administrator?,
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    val room: Room,
    @Column(name = "check_in_date")
    @field:NotNull(message = "Check in date shouldn't be null!")
    val checkIn: Date,
    @Column(name = "check_out_date")
    @field:NotNull(message = "Check out date shouldn't be null!")
    val checkOut: Date,
    @field:NotNull(message = "Total price shouldn't be null!")
    val totalPrice: Double,
    var isAccepted: Boolean?,
    @Column(name = "_comment")
    var comment: String?,
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    val client: Client,
    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    val payment: Payment,
    @ManyToMany(targetEntity = Service::class, fetch = FetchType.LAZY)
    @JoinTable(
        name = "service_registration",
        joinColumns = [JoinColumn(name = "registration_id", referencedColumnName = "registration_id")],
        inverseJoinColumns = [JoinColumn(name = "service_id", referencedColumnName = "service_id")]
    )
    val services: List<Service>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Registration

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , room = $room , checkIn = $checkIn , checkOut = $checkOut , totalPrice = $totalPrice )"
    }
}