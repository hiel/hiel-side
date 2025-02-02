package com.hiel.hielside.accountbook.apis.home

import com.hiel.hielside.accountbook.domains.Range
import java.time.OffsetDateTime

data class GetHomeResponse(
    val transactionMonthlyRange: Range<OffsetDateTime>,
    val totalExpense: Long,
    val balance: Long?,
    val availableExpensePricePerDay: Long?,
    val isFine: Boolean?,
    val assetCategories: List<AssetCategoryDetail>,
    val transactionCategories: List<TransactionCategoryDetail>,
    val wastedPrice: Long,
) {
    data class AssetCategoryDetail(
        val id: Long,
        val name: String,
        val budget: Long?,
        val totalExpense: Long,
        val balance: Long?,
        val availableExpensePricePerDay: Long?,
        val isFine: Boolean?,
    )

    data class TransactionCategoryDetail(
        val id: Long,
        val name: String,
        val totalExpense: Long,
    )
}
