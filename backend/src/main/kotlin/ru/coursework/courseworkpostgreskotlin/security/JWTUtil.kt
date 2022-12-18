package ru.coursework.courseworkpostgreskotlin.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.Date

/**
 * @author Vlad Utts
 */
@Component
class JWTUtil {
    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    fun generateToken(username: String): String {
        val expirationDate: Date = Date.from(ZonedDateTime.now().plusDays(1).toInstant())
        return JWT.create()
            .withSubject("User details")
            .withClaim("username", username)
            .withIssuedAt(Date())
            .withIssuer("courseWork")
            .withExpiresAt(expirationDate)
            .sign(Algorithm.HMAC256(secretKey))
    }

    fun validateTokenAndRetrieveClaim(token: String): String =
        JWT.require(Algorithm.HMAC256(secretKey))
            .withSubject("User details")
            .withIssuer("courseWork")
            .build()
            .verify(token)
            .getClaim("username").asString()
}