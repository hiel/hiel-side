package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import com.hiel.hielside.common.utilities.getNowKst
import com.hiel.hielside.common.utilities.initializeTime
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

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
    fun getSlice(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestParam("page") page: Int,
        @RequestParam("pageSize") pageSize: Int,
        @RequestParam("transactionDateTime") transactionDateTime: OffsetDateTime? = null,
    ): ApiResponse<GetAllTransactionResponse> {
        val datetime = (transactionDateTime ?: getNowKst()).initializeTime()
        return ApiResponseFactory.success(
            GetAllTransactionResponse.build(
                slice = transactionService.getSlice(
                    transactionDatetime = datetime,
                    page = page,
                    pageSize = pageSize,
                    userId = userDetails.id,
                ),
                transactionDatetime = datetime,
            )
        )
    }
}
