package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size
import kotlin.math.min

/**
 * @author Vlad Utts
 */
@Entity
data class WindowView(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "window_view_id")
    val id: Long,
    @Column(name = "window_view_name")
    @field:NotEmpty(message = "Window view name shouldn't be empty!")
    @field:Size(min = 3, max = 30, message = "Window view name size should be between 3 and 30 characters!")
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as WindowView

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name )"
    }
}
