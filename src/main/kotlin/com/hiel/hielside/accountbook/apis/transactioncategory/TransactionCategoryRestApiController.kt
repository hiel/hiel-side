package com.hiel.hielside.accountbook.apis.transactioncategory

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
        @RequestBody request: UpdateTransactionCategoryRequest,
    ): ApiResponse<Unit> {
        transactionCategoryService.update(transactionCategoryId = id, name = request.name, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
    ): ApiResponse<Unit> {
        transactionCategoryService.delete(transactionCategoryId = id, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @GetMapping("")
    fun getAll(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ApiResponse<GetAllTransactionCategoryResponse> {
        return ApiResponseFactory.success(
            GetAllTransactionCategoryResponse.build(transactionCategoryService.getAll(userId = userDetails.id))
        )
    }
}
