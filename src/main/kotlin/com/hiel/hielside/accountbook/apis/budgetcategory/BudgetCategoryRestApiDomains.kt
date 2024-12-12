package com.hiel.hielside.accountbook.apis.budgetcategory

import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryEntity

data class RegisterBudgetCategoryRequest(
    val name: String,
)

data class GetAllBudgetCategoryResponse(
    val list: List<GetAllBudgetCategoryDetail>
) {
    data class GetAllBudgetCategoryDetail(
        val id: Long,
        val name: String,
    ) {
        companion object {
            fun build(budgetCategory: BudgetCategoryEntity) = GetAllBudgetCategoryDetail(
                id = budgetCategory.id,
                name = budgetCategory.name,
            )
        }
    }

    companion object {
        fun build(budgetCategories: List<BudgetCategoryEntity>) = GetAllBudgetCategoryResponse(
            list = budgetCategories.map { GetAllBudgetCategoryDetail.build(it) },
        )
    }
}
