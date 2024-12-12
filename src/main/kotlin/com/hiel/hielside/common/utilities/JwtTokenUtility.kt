package com.hiel.hielside.common.utilities

import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.auth.AuthToken
import com.hiel.hielside.common.domains.auth.TokenType
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class JwtTokenUtility(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
) {
    companion object {
        const val CLAIM_KEY_TOKEN_TYPE = "tokenType"
    }

    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateAuthToken(user: AccountBookUserEntity): AuthToken {
        val claims = mapOf(
            "id" to user.id,
            "email" to user.email,
            "name" to user.name,
            "userType" to user.userType,
        )
        return AuthToken(
            accessToken = generateToken(
                claims = claims,
                tokenType = TokenType.ACCESS_TOKEN,
                expireDuration = AuthToken.ACCESS_TOKEN_EXPIRE_DURATION,
            ),
            refreshToken = generateToken(
                claims = claims,
                tokenType = TokenType.REFRESH_TOKEN,
                expireDuration = AuthToken.REFRESH_TOKEN_EXPIRE_DURATION,
            ),
        )
    }

    fun generateToken(claims: Map<String, *>, tokenType: TokenType, expireDuration: Duration): String {
        return Jwts.builder()
            .claims(claims)
            .claim(CLAIM_KEY_TOKEN_TYPE, tokenType.name)
            .issuedAt(getNowKst().toDate())
            .expiration(getNowKst().plus(expireDuration).toDate())
            .signWith(key)
            .compact()
    }

    fun parseToken(token: String, tokenType: TokenType): UserDetailsImpl {
        try {
            val parsed = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload

            if (tokenType.name != parsed[CLAIM_KEY_TOKEN_TYPE]) {
                throw MalformedJwtException("Invalid token type (${parsed[CLAIM_KEY_TOKEN_TYPE]})")
            }

            return UserDetailsImpl(
                id = (parsed["id"] as Int).toLong(),
                email = parsed["email"] as String,
                name = parsed["name"] as String,
                userType = UserType.valueOf(parsed["userType"] as String),
            )
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ServiceException(ResultCode.Auth.EXPIRED_ACCESS_TOKEN)
                is MalformedJwtException,
                is SignatureException,
                -> throw ServiceException(ResultCode.Auth.INVALID_TOKEN)
                else -> throw ServiceException(ResultCode.Auth.AUTHENTICATION_FAIL)
            }
        }
    }
}
