package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryEntity
import com.hiel.hielside.accountbook.jpa.transaction.TransactionEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.common.utilities.toFormatString
import com.hiel.hielside.common.utilities.toOffsetDateTime

data class RegisterTransactionRequest(
    val incomeExpenseType: IncomeExpenseType,
    val title: String,
    val price: Long,
    val isWaste: Boolean = false,
    val budgetCategoryId: Long,
    val transactionCategoryId: Long,
    val transactionDate: String,
) {
    fun build(
        user: AccountBookUserEntity,
        budgetCategory: BudgetCategoryEntity,
        transactionCategory: TransactionCategoryEntity,
    ) = TransactionEntity(
        incomeExpenseType = incomeExpenseType,
        title = title,
        price = price,
        isWaste = isWaste,
        user = user,
        budgetCategory = budgetCategory,
        transactionCategory = transactionCategory,
        transactionDatetime = transactionDate.toOffsetDateTime("yyyyMMdd"),
    )
}

data class GetAllTransactionResponse(
    val id: Long,
    val date: String,
    val budgetCategoryId: Long,
    val budgetCategoryName: String,
    val transactionCategoryId: Long,
    val transactionCategoryName: String,
    val title: String,
    val price: Long,
    val isWaste: Boolean,
) {
    companion object {
        fun build(transaction: TransactionEntity) = GetAllTransactionResponse(
            id = transaction.id,
            date = transaction.transactionDatetime.toFormatString(format = "yyyyMMdd"),
            budgetCategoryId = transaction.budgetCategory.id,
            budgetCategoryName = transaction.budgetCategory.name,
            transactionCategoryId = transaction.transactionCategory.id,
            transactionCategoryName = transaction.transactionCategory.name,
            title = transaction.title,
            price = transaction.price,
            isWaste = transaction.isWaste,
        )
    }
}
