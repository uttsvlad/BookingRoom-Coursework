package ru.coursework.courseworkpostgreskotlin.config

import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.coursework.courseworkpostgreskotlin.security.JWTUtil
import ru.coursework.courseworkpostgreskotlin.service.UserDetailsService
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Vlad Utts
 */
@Component
class JWTFilter(private val jwtUtil: JWTUtil, private val userDetailsService: UserDetailsService) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
            val jwt = authHeader.substring(7)

            if (jwt.isBlank())
                return response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid JWT!"
                )
            else {
                try {
                    val username = jwtUtil.validateTokenAndRetrieveClaim(jwt)
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    val authToken =
                        UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)

                    if (SecurityContextHolder.getContext().authentication == null)
                        SecurityContextHolder.getContext().authentication = authToken
                } catch (e: JWTVerificationException) {
                    return response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT!"
                    )
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}