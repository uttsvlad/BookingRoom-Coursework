package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
@Entity
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    val id: Long,
    @Column(name = "category_name")
    @field:NotEmpty(message = "Category name shouldn't be empty!")
    @field:Size(min = 3, max = 50, message = "Category name size should be between 3 and 50 characters!")
    var name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Category

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name )"
    }
}
