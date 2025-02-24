package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import com.hiel.hielside.common.utilities.DateTimeFormat
import com.hiel.hielside.common.utilities.getNowKst
import com.hiel.hielside.common.utilities.toOffsetDateTime
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/transactions")
@RestController
class TransactionRestApiController(
    private val transactionService: TransactionService,
) {
    @PostMapping
    fun register(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestBody request: RegisterTransactionRequest,
    ): ApiResponse<Unit> {
        request.validate()
        transactionService.register(request = request, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
        @RequestBody request: UpdateTransactionRequest,
    ): ApiResponse<Unit> {
        request.validate()
        transactionService.update(transactionId = id, request = request, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
    ): ApiResponse<Unit> {
        transactionService.delete(transactionId = id, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @GetMapping("/{id}")
    fun getDetail(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
    ): ApiResponse<GetTransactionDetailResponse> {
        return ApiResponseFactory.success(
            GetTransactionDetailResponse.build(transactionService.getDetail(transactionId = id, userId = userDetails.id)))
    }

    @GetMapping
    fun getSlice(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestParam("page") page: Int,
        @RequestParam("pageSize") pageSize: Int,
        @RequestParam("date") date: String? = null,
    ): ApiResponse<GetAllTransactionResponse> {
        return ApiResponseFactory.success(
            transactionService.getSlice(
                datetime = date?.toOffsetDateTime(DateTimeFormat.DATE_BAR) ?: getNowKst(),
                page = page,
                pageSize = pageSize,
                userId = userDetails.id,
            ),
        )
    }
}
