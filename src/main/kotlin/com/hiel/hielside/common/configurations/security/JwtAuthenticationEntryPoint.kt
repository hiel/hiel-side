package com.hiel.hielside.common.configurations.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtAuthenticationEntryPoint(
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver,
) : AuthenticationEntryPoint {
    companion object {
        const val EXCEPTION_ATTRIBUTE_NAME: String = "exception"
    }

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        resolver.resolveException(request, response, null, request.getAttribute(EXCEPTION_ATTRIBUTE_NAME) as Exception)
    }
}
