package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * @author Vlad Utts
 */
@Entity
data class Room(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    val id: Long,
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    val category: Category,
    @Column(name = "room_price")
    @field:NotNull(message = "Room price shouldn't be null!")
    val price: Double,
    @Column(name = "room_capacity")
    @field:NotNull(message = "Room capacity shouldn't be null!")
    val capacity: Int,
    @Column(name = "room_floor")
    @field:NotNull(message = "Room floor shouldn't be null!")
    val floor: Int,
    @ManyToOne
    @JoinColumn(name = "window_view_id", referencedColumnName = "window_view_id")
    val windowView: WindowView,
    @Column(name = "conditioner_availability")
    @field:NotNull(message = "Conditioner availability shouldn't be null!")
    val conditionerAvailable: Boolean,
    @Column(name = "hair_dryer_availability")
    @field:NotNull(message = "Hair dryer availability shouldn't be null!")
    val hairDryerAvailable: Boolean,
    @Column(name = "description")
    @NotEmpty(message = "Description shouldn't be empty!")
    val description: String,
    @ManyToMany(targetEntity = Photo::class)
    @JoinTable(
        name = "room_photo",
        joinColumns = [JoinColumn(name = "room_id", referencedColumnName = "room_id")],
        inverseJoinColumns = [JoinColumn(name = "photo_id", referencedColumnName = "photo_id")]
    )
    val photos: List<Photo>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Room

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , category = $category , price = $price , capacity = $capacity , floor = $floor , windowView = $windowView , conditionerAvailable = $conditionerAvailable , hairDryerAvailable = $hairDryerAvailable )"
    }
}