package com.hiel.hielside.common.configurations.security

import com.hiel.hielside.common.filters.JwtAuthenticationFilter
import com.hiel.hielside.common.filters.RequestResponseLoggingFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@Configuration
class SecurityConfiguration(
    @Value("\${allow-origin-urls}")
    private val allowOriginUrls: List<String>,
) {
    companion object {
        data class PermitUrl(
            val url: String,
            val method: HttpMethod? = null,
            val optional: Boolean = false,
        )

        val PERMIT_URLS = arrayOf(
            PermitUrl(url = "/account-book/developers/**"),
            PermitUrl(url = "/auths/**"),
        )
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        requestResponseLoggingFilter: RequestResponseLoggingFilter,
        jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    ): SecurityFilterChain {
        return httpSecurity
            .authorizeHttpRequests { auth ->
                PERMIT_URLS.forEach { permitUrl ->
                    if (permitUrl.optional) {
                        return@forEach
                    }
                    if (permitUrl.method != null) {
                        auth.requestMatchers(antMatcher(permitUrl.method), antMatcher(permitUrl.url)).permitAll()
                    } else {
                        auth.requestMatchers(antMatcher(permitUrl.url)).permitAll()
                    }
                }
                auth.anyRequest().authenticated()
            }
            .cors {}
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { it.authenticationEntryPoint(jwtAuthenticationEntryPoint) }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = allowOriginUrls
        configuration.allowedMethods = listOf(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).map { it.name() }
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
