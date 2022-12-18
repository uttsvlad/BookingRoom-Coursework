package ru.coursework.courseworkpostgreskotlin.model

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vlad Utts
 */
@Entity
data class Complaint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    val id: Long,
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    var client: Client,
    @Column(name = "complaint_content")
    @field:NotEmpty(message = "Complaint's content shouldn't be empty!")
    @field:Size(min = 5, max = 300, message = "Complaint's content size should be between 5 and 300 characters!")
    var content: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Complaint

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , client = $client , complaintContent = $content )"
    }

}