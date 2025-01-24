package com.hiel.hielside.accountbook.apis.home

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import com.hiel.hielside.common.utilities.ApiResponseFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book")
@RestController
class AccountBookHomeController(
    private val accountBookHomeService: AccountBookHomeService,
) {
    @GetMapping("/home")
    fun getHome(@AuthenticationPrincipal userDetails: UserDetailsImpl): ApiResponse<GetHomeResponse> {
        return ApiResponseFactory.success(accountBookHomeService.getHome(userDetails.id))
    }
}
