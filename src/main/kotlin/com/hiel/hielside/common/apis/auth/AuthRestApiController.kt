package com.hiel.hielside.common.apis.auth

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auths")
@RestController
class AuthRestApiController(
    private val authService: AuthService,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostMapping("/signup")
    fun signup(
        @RequestBody request: SignupRequest,
    ): ApiResponse<Unit> {
        request.validate()
        authService.signup(
            serviceType = request.serviceType,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            userType = request.userType,
        )
        return ApiResponseFactory.success()
    }

    @PutMapping("/signup/certificate")
    fun certificateSignup(
        @RequestBody request: CertificateSignupRequest,
    ): ApiResponse<Unit> {
        authService.certificateSignup(request.signupToken)
        return ApiResponseFactory.success()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ApiResponse<IssueTokenResponse> {
        return ApiResponseFactory.success(authService.login(email = request.email, password = request.password))
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest,
    ): ApiResponse<IssueTokenResponse> {
        return ApiResponseFactory.success(authService.refreshToken(request.refreshToken))
    }

    @PostMapping("/password/reset/request")
    fun requestPasswordReset(
        @RequestBody request: RequestPasswordResetRequest,
    ): ApiResponse<Unit> {
        authService.requestPasswordReset(request.email)
        return ApiResponseFactory.success()
    }

    @PutMapping("/password/reset")
    fun resetPassword(
        @RequestBody request: ResetPasswordRequest,
    ): ApiResponse<ResetPasswordResponse> {
        return ApiResponseFactory.success(
            ResetPasswordResponse(authService.resetPassword(request.resetPasswordToken)),
        )
    }
}
