package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
@Entity
@Table(name = "_user")
data class User(
    @Id
    @field:NotEmpty(message = "Username shouldn't be empty!")
    @field:Size(min = 3, max = 30, message = "Username size should be between 3 and 30 characters!")
    val username: String,
    @field:NotEmpty(message = "Password shouldn't be empty!")
    @field:Size(min = 8, max = 100, message = "Password size should be between 8 and 100 characters!")
    val password: String,
    val role: String,
    @Column(name = "registration_date")
    @field:NotNull(message = "Registration date shouldn't be null!")
    val registrationDate: Date,
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    val client: Client?,
    @ManyToOne
    @JoinColumn(name = "administrator_id", referencedColumnName = "administrator_id")
    val administrator: Administrator?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return username == other.username
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(username = $username , password = $password , role = $role , client = $client , administrator = $administrator )"
    }
}