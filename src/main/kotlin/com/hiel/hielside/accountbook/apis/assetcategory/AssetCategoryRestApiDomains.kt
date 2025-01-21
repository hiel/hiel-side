package com.hiel.hielside.accountbook.apis.assetcategory

import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryEntity

data class RegisterAssetCategoryRequest(
    val name: String,
)

data class UpdateAssetCategoryRequest(
    val name: String,
)

data class GetAllAssetCategoryResponse(
    val list: List<GetAllAssetCategoryDetail>
) {
    data class GetAllAssetCategoryDetail(
        val id: Long,
        val name: String,
    ) {
        companion object {
            fun build(assetCategory: AssetCategoryEntity) = GetAllAssetCategoryDetail(
                id = assetCategory.id,
                name = assetCategory.name,
            )
        }
    }

    companion object {
        fun build(assetCategories: List<AssetCategoryEntity>) = GetAllAssetCategoryResponse(
            list = assetCategories.map { GetAllAssetCategoryDetail.build(it) },
        )
    }
}
