package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/transactions")
@RestController
class TransactionRestApiController(
    private val transactionService: TransactionService,
) {
    @PostMapping("")
    fun register(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestBody request: RegisterTransactionRequest,
    ): ApiResponse<Unit> {
        transactionService.register(
            request = request,
            userId = userDetails.id,
        )
        return ApiResponseFactory.success()
    }
}
