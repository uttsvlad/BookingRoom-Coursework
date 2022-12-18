package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
@Entity
class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    val id: Long,
    @field:NotEmpty(message = "Surname shouldn't be empty!")
    @field:Size(min = 1, max = 150, message = "Surname's size should be between 1 and 150 characters!")
    var surname: String,
    @field:NotEmpty(message = "Firstname shouldn't be empty!")
    @field:Size(min = 1, max = 150, message = "Firstname's size should be between 1 and 150 characters!")
    var firstName: String,
    var middleName: String?,
    @field:NotEmpty(message = "Document name shouldn't be empty!")
    @field:Size(min = 7, max = 14, message = "Document name's size should be between 7 and 14 characters!")
    var documentName: String,
    @field:NotEmpty(message = "Document name shouldn't be empty!")
    @field:Size(min = 3, max = 50, message = "Document name's size should be between 3 and 50 characters!")
    var documentNumber: String,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Client

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , surname = $surname , firstName = $firstName , middleName = $middleName , documentName = $documentName , documentNumber = $documentNumber)"
    }
}