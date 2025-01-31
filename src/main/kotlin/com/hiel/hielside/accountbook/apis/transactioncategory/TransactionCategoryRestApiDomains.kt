package com.hiel.hielside.accountbook.apis.transactioncategory

import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity

data class RegisterTransactionCategoryRequest(
    val name: String,
)

data class UpdateTransactionCategoryRequest(
    val name: String,
)

data class GetAllTransactionCategoryResponse(
    val list: List<GetAllTransactionCategoryDetail>
) {
    data class GetAllTransactionCategoryDetail(
        val id: Long,
        val name: String,
    ) {
        companion object {
            fun build(transactionCategory: TransactionCategoryEntity) = GetAllTransactionCategoryDetail(
                id = transactionCategory.id,
                name = transactionCategory.name,
            )
        }
    }

    companion object {
        fun build(transactionCategories: List<TransactionCategoryEntity>) = GetAllTransactionCategoryResponse(
            list = transactionCategories.map { GetAllTransactionCategoryDetail.build(it) },
        )
    }
}
