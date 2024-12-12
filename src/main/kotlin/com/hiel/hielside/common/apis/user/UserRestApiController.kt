package com.hiel.hielside.common.apis.user

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserRestApiController(
    private val userService: UserService,
) {
    @PutMapping("/password")
    fun updatePassword(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestBody request: UpdatePasswordRequest,
    ): ApiResponse<Unit> {
        request.validate()
        userService.updatePassword(
            currentPassword = request.currentPassword,
            updatePassword = request.updatePassword,
            userId = userDetails.id,
        )
        return ApiResponseFactory.success()
    }
}
