package com.hiel.hielside.accountbook.apis.assetcategory

import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryEntity

data class RegisterAssetCategoryRequest(
    val name: String,
    val budgetPrice: Long? = null,
)

data class UpdateAssetCategoryRequest(
    val name: String,
    val budgetPrice: Long? = null,
)

data class GetAllAssetCategoryResponse(
    val list: List<GetAllAssetCategoryDetail>,
) {
    data class GetAllAssetCategoryDetail(
        val id: Long,
        val name: String,
        val budgetPrice: Long?,
    ) {
        companion object {
            fun build(assetCategory: AssetCategoryEntity) = GetAllAssetCategoryDetail(
                id = assetCategory.id,
                name = assetCategory.name,
                budgetPrice = assetCategory.budgetPrice,
            )
        }
    }

    companion object {
        fun build(assetCategories: List<AssetCategoryEntity>) = GetAllAssetCategoryResponse(
            list = assetCategories.map { GetAllAssetCategoryDetail.build(it) },
        )
    }
}
