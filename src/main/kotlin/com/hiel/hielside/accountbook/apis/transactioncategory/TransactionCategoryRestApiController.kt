package com.hiel.hielside.accountbook.apis.transactioncategory

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/transaction-categories")
@RestController
class TransactionCategoryRestApiController(
    private val transactionCategoryService: TransactionCategoryService,
) {
    @PostMapping("")
    fun register(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestBody request: RegisterTransactionCategoryRequest,
    ): ApiResponse<Unit> {
        transactionCategoryService.register(
            name = request.name,
            userId = userDetails.id,
        )
        return ApiResponseFactory.success()
    }
}
