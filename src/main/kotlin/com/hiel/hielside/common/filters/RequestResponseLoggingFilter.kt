package com.hiel.hielside.common.filters

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hiel.hielside.common.utilities.LogUtility
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestResponseLoggingFilter : OncePerRequestFilter() {
    companion object : LogUtility

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain) {
        val objectMapper = jacksonObjectMapper()
        val reqWrapper = ContentCachingRequestWrapper(req)
        val resWrapper = ContentCachingResponseWrapper(res)

        val startTime = System.currentTimeMillis()
        filterChain.doFilter(reqWrapper, resWrapper)
        val endTime = System.currentTimeMillis()

        try {
            log.info(
                objectMapper.writeValueAsString(
                    mapOf(
                        "request" to mapOf(
                            "method" to reqWrapper.method,
                            "url" to reqWrapper.requestURL,
                            "queryString" to reqWrapper.queryString,
                            "body" to String(reqWrapper.contentAsByteArray, Charsets.UTF_8),
                            "headers" to reqWrapper.headerNames.toList().associateWith { reqWrapper.getHeaders(it).toList() },
                            "time" to "${((endTime - startTime) / 1000.0)}s",
                        ),
                    ),
                ),
            )
            log.info(
                objectMapper.writeValueAsString(
                    mapOf(
                        "response" to mapOf(
                            "statusCode" to resWrapper.status,
                            "body" to String(resWrapper.contentAsByteArray, Charsets.UTF_8),
                            "headers" to resWrapper.headerNames.toList().associateWith { resWrapper.getHeaders(it).toList() },
                        ),
                    ),
                ),
            )
            resWrapper.copyBodyToResponse()
        } catch (e: Exception) {
            log.error(e.toString())
        }
    }
}
