package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryEntity
import com.hiel.hielside.accountbook.jpa.transaction.TransactionEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.SliceResponseData
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.utilities.DateTimeFormat
import com.hiel.hielside.common.utilities.toFormatString
import org.springframework.data.domain.Slice
import java.time.OffsetDateTime

data class RegisterTransactionRequest(
    val incomeExpenseType: IncomeExpenseType,
    val title: String,
    val price: Long,
    val isWaste: Boolean = false,
    val assetCategoryId: Long,
    val transactionCategoryId: Long,
    val transactionDate: OffsetDateTime,
) {
    fun validate() {
        if (incomeExpenseType == IncomeExpenseType.INCOME && isWaste) {
            throw ServiceException(ResultCode.Transaction.INCOME_INVALID_IS_WASTE)
        }
    }

    fun build(
        user: AccountBookUserEntity,
        assetCategory: AssetCategoryEntity,
        transactionCategory: TransactionCategoryEntity,
    ) = TransactionEntity(
        incomeExpenseType = incomeExpenseType,
        title = title,
        price = price,
        isWaste = isWaste,
        user = user,
        assetCategory = assetCategory,
        transactionCategory = transactionCategory,
        transactionDatetime = transactionDate,
    )
}

data class UpdateTransactionRequest(
    val incomeExpenseType: IncomeExpenseType,
    val title: String,
    val price: Long,
    val isWaste: Boolean = false,
    val assetCategoryId: Long,
    val transactionCategoryId: Long,
    val transactionDate: OffsetDateTime,
) {
    fun validate() {
        if (incomeExpenseType == IncomeExpenseType.INCOME && isWaste) {
            throw ServiceException(ResultCode.Transaction.INCOME_INVALID_IS_WASTE)
        }
    }
}

data class GetTransactionDetailResponse(
    val id: Long,
    val date: String,
    val price: Long,
    val title: String,
    val assetCategoryId: Long,
    val assetCategoryName: String,
    val assetCategoryBudgetPrice: Long?,
    val transactionCategoryId: Long,
    val transactionCategoryName: String,
    val transactionCategoryBudgetPrice: Long?,
    val incomeExpenseType: IncomeExpenseType,
    val isWaste: Boolean,
) {
    companion object {
        fun build(transaction: TransactionEntity) = GetTransactionDetailResponse(
            id = transaction.id,
            date = transaction.transactionDatetime.toFormatString(),
            price = transaction.price,
            title = transaction.title,
            assetCategoryId = transaction.assetCategory.id,
            assetCategoryName = transaction.assetCategory.name,
            assetCategoryBudgetPrice = transaction.assetCategory.budgetPrice,
            transactionCategoryId = transaction.transactionCategory.id,
            transactionCategoryName = transaction.transactionCategory.name,
            transactionCategoryBudgetPrice = transaction.transactionCategory.budgetPrice,
            incomeExpenseType = transaction.incomeExpenseType,
            isWaste = transaction.isWaste,
        )
    }
}

data class GetAllTransactionResponse(
    val slice: SliceResponseData<GetAllTransactionResponseDetail>,
    val transactionMonthlyRange: List<OffsetDateTime>,
) {
    data class GetAllTransactionResponseDetail(
        val id: Long,
        val date: String,
        val price: Long,
        val title: String,
        val assetCategoryId: Long,
        val assetCategoryName: String,
        val transactionCategoryId: Long,
        val transactionCategoryName: String,
        val incomeExpenseType: IncomeExpenseType,
        val isWaste: Boolean,
    ) {
        companion object {
            fun build(transaction: TransactionEntity) = GetAllTransactionResponseDetail(
                id = transaction.id,
                date = transaction.transactionDatetime.toFormatString(format = DateTimeFormat.DATE),
                price = transaction.price,
                title = transaction.title,
                assetCategoryId = transaction.assetCategory.id,
                assetCategoryName = transaction.assetCategory.name,
                transactionCategoryId = transaction.transactionCategory.id,
                transactionCategoryName = transaction.transactionCategory.name,
                incomeExpenseType = transaction.incomeExpenseType,
                isWaste = transaction.isWaste,
            )
        }
    }

    companion object {
        fun build(
            slice: Slice<TransactionEntity>, user: AccountBookUserEntity, transactionDatetime: OffsetDateTime,
        ): GetAllTransactionResponse {
            return GetAllTransactionResponse(
                slice = SliceResponseData.build(
                    slice = slice,
                    content = slice.content.map { GetAllTransactionResponseDetail.build(it) },
                ),
                transactionMonthlyRange = user.getTransactionMonthlyRange(transactionDatetime).toList(),
            )
        }
    }
}

