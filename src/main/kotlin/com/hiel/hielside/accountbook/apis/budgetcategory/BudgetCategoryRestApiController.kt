package com.hiel.hielside.accountbook.apis.budgetcategory

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

@RequestMapping("/account-book/budget-categories")
@RestController
class BudgetCategoryRestApiController(
    private val budgetCategoryService: BudgetCategoryService,
) {
    @PostMapping("")
    fun register(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestBody request: RegisterBudgetCategoryRequest,
    ): ApiResponse<Unit> {
        budgetCategoryService.register(
            name = request.name,
            userId = userDetails.id,
        )
        return ApiResponseFactory.success()
    }

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
        @RequestBody request: UpdateBudgetCategoryRequest,
    ): ApiResponse<Unit> {
        budgetCategoryService.update(budgetCategoryId = id, name = request.name, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
    ): ApiResponse<Unit> {
        budgetCategoryService.delete(budgetCategoryId = id, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @GetMapping("")
    fun getAll(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ApiResponse<GetAllBudgetCategoryResponse> {
        return ApiResponseFactory.success(
            GetAllBudgetCategoryResponse.build(budgetCategoryService.getAll(userId = userDetails.id))
        )
    }
}
