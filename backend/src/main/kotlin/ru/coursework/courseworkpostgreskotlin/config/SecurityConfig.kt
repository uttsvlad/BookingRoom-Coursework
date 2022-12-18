package ru.coursework.courseworkpostgreskotlin.config

import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * @author Vlad Utts
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(private val jwtFilter: JWTFilter) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeRequests().antMatchers("/login", "/registration", "/admin_statistics.csv").permitAll()
            .antMatchers("/superAdmin/**").hasRole("SUPER_ADMIN")
            .antMatchers("/administrator/**").hasRole("ADMINISTRATOR")
            .anyRequest().hasAnyRole("CLIENT", "ADMINISTRATOR", "SUPER_ADMIN")
            .and().sessionManagement().disable()
            .logout().logoutUrl("/logout")
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.addFilterBefore(
            jwtFilter, UsernamePasswordAuthenticationFilter::class.java
        )
        return http.build()
    }

    @Bean
    fun authenticationManager(auth: AuthenticationConfiguration): AuthenticationManager = auth.authenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}