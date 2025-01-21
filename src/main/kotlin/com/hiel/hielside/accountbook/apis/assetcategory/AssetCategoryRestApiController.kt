package com.hiel.hielside.accountbook.apis.assetcategory

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

@RequestMapping("/account-book/asset-categories")
@RestController
class AssetCategoryRestApiController(
    private val assetCategoryService: AssetCategoryService,
) {
    @PostMapping("")
    fun register(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @RequestBody request: RegisterAssetCategoryRequest,
    ): ApiResponse<Unit> {
        assetCategoryService.register(name = request.name, budgetPrice = request.budgetPrice, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
        @RequestBody request: UpdateAssetCategoryRequest,
    ): ApiResponse<Unit> {
        assetCategoryService.update(assetCategoryId = id, name = request.name, budgetPrice = request.budgetPrice, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
        @PathVariable id: Long,
    ): ApiResponse<Unit> {
        assetCategoryService.delete(assetCategoryId = id, userId = userDetails.id)
        return ApiResponseFactory.success()
    }

    @GetMapping("")
    fun getAll(
        @AuthenticationPrincipal userDetails: UserDetailsImpl,
    ): ApiResponse<GetAllAssetCategoryResponse> {
        return ApiResponseFactory.success(
            GetAllAssetCategoryResponse.build(assetCategoryService.getAll(userId = userDetails.id))
        )
    }
}
