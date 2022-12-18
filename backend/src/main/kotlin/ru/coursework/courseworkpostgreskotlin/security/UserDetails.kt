package ru.coursework.courseworkpostgreskotlin.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.coursework.courseworkpostgreskotlin.model.User

/**
 * @author Vlad Utts
 */
class UserDetails(private val user: User) :
    UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority(user.role))

    override fun getPassword() = user.password

    override fun getUsername() = user.username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}