package com.hiel.hielside.accountbook.apis.user

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/users")
@RestController
class AccountBookUserRestApiController(
    private val userService: AccountBookUserService,
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

    @PutMapping("/transaction-start-day")
    fun updateTransactionStartDay(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestBody request: UpdateTransactionStartDayRequest,
    ): ApiResponse<UpdateTransactionStartDayResponse> {
        request.validate()
        return ApiResponseFactory.success(UpdateTransactionStartDayResponse(
            userService.updateTransactionStartDay(startDay = request.transactionStartDay, userId = userDetails.id)))
    }

    @GetMapping
    fun getUser(@AuthenticationPrincipal userDetails: UserDetailsImpl): ApiResponse<GetUserResponse> {
        return ApiResponseFactory.success(userService.getUser(userId = userDetails.id))
    }
}
