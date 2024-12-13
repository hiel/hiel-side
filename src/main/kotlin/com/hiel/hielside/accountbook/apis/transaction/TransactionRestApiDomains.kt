package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryEntity
import com.hiel.hielside.accountbook.jpa.transaction.TransactionEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.common.domains.SliceResponseData
import com.hiel.hielside.common.utilities.toFormatString
import org.springframework.data.domain.Slice
import java.time.OffsetDateTime

data class RegisterTransactionRequest(
    val incomeExpenseType: IncomeExpenseType,
    val title: String,
    val price: Long,
    val isWaste: Boolean = false,
    val budgetCategoryId: Long,
    val transactionCategoryId: Long,
    val transactionDate: OffsetDateTime,
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
        transactionDatetime = transactionDate,
    )
}

data class GetAllTransactionResponse(
    val slice: SliceResponseData<GetAllTransactionResponseDetail>,
    val transactionDatetime: OffsetDateTime,
) {
    data class GetAllTransactionResponseDetail(
        val id: Long,
        val date: String,
        val budgetCategoryId: Long,
        val budgetCategoryName: String,
        val transactionCategoryId: Long,
        val transactionCategoryName: String,
        val title: String,
        val price: Long,
        val incomeExpenseType: IncomeExpenseType,
        val isWaste: Boolean,
    ) {
        companion object {
            fun build(transaction: TransactionEntity) = GetAllTransactionResponseDetail(
                id = transaction.id,
                date = transaction.transactionDatetime.toFormatString(format = "yyyyMMdd"),
                budgetCategoryId = transaction.budgetCategory.id,
                budgetCategoryName = transaction.budgetCategory.name,
                transactionCategoryId = transaction.transactionCategory.id,
                transactionCategoryName = transaction.transactionCategory.name,
                title = transaction.title,
                price = transaction.price,
                incomeExpenseType = transaction.incomeExpenseType,
                isWaste = transaction.isWaste,
            )
        }
    }

    companion object {
        fun build(slice: Slice<TransactionEntity>, transactionDatetime: OffsetDateTime): GetAllTransactionResponse {
            return GetAllTransactionResponse(
                slice = SliceResponseData.build(
                    slice = slice,
                    content = slice.content.map { GetAllTransactionResponseDetail.build(it) },
                ),
                transactionDatetime = transactionDatetime,
            )
        }
    }
}

