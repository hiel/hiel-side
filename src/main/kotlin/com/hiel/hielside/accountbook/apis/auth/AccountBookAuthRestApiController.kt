package com.hiel.hielside.accountbook.apis.auth

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/auths")
@RestController
class AccountBookAuthRestApiController(
    private val authService: AccountBookAuthService,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostMapping("/signup")
    fun signup(
        @RequestBody request: AccountBookSignupRequest,
    ): ApiResponse<Unit> {
        request.validate()
        authService.signup(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            userType = UserType.USER,
        )
        return ApiResponseFactory.success()
    }

    @PutMapping("/signup/certificate")
    fun certificateSignup(
        @RequestBody request: AccountBookCertificateSignupRequest,
    ): ApiResponse<Unit> {
        authService.certificateSignup(request.signupToken)
        return ApiResponseFactory.success()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: AccountBookLoginRequest,
    ): ApiResponse<AccountBookIssueTokenResponse> {
        return ApiResponseFactory.success(authService.login(email = request.email, password = request.password))
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestBody request: AccountBookRefreshTokenRequest,
    ): ApiResponse<AccountBookIssueTokenResponse> {
        return ApiResponseFactory.success(authService.refreshToken(request.refreshToken))
    }

    @PostMapping("/password/reset/request")
    fun requestPasswordReset(
        @RequestBody request: AccountBookRequestPasswordResetRequest,
    ): ApiResponse<Unit> {
        authService.requestPasswordReset(request.email)
        return ApiResponseFactory.success()
    }

    @PutMapping("/password/reset")
    fun resetPassword(
        @RequestBody request: AccountBookResetPasswordRequest,
    ): ApiResponse<AccountBookResetPasswordResponse> {
        return ApiResponseFactory.success(AccountBookResetPasswordResponse(authService.resetPassword(request.resetPasswordToken)))
    }
}
