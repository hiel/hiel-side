package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.ApiSliceResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import com.hiel.hielside.common.utilities.getNowKst
import com.hiel.hielside.common.utilities.toFormatString
import com.hiel.hielside.common.utilities.toOffsetDateTime
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("")
    fun getAll(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestParam("page") page: Int,
        @RequestParam("pageSize") pageSize: Int,
        @RequestParam("transactionYearMonth") transactionYearMonth: String? = null,
    ): ApiSliceResponse<GetAllTransactionResponse> {
        var yearMonth = transactionYearMonth
        if (transactionYearMonth == null) {
            yearMonth = getNowKst().toFormatString("yyyyMM")
        }

        return ApiResponseFactory.sliceOf(
            list = transactionService.getAll(
                transactionYearMonth = "${yearMonth}01".toOffsetDateTime("yyyyMMdd"),
                page = page,
                pageSize = pageSize,
                userId = userDetails.id,
            ).map { GetAllTransactionResponse.build(it) },
        )
    }
}
