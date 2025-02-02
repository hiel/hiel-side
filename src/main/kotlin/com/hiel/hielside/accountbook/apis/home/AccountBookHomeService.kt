package com.hiel.hielside.accountbook.apis.home

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.domains.toRange
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transaction.TransactionRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.utilities.getNowKst
import com.hiel.hielside.common.utilities.untilInitializeTime
import java.time.temporal.ChronoUnit
import org.springframework.stereotype.Service

@Service
class AccountBookHomeService(
    private val userRepository: AccountBookUserRepository,
    private val assetCategoryRepository: AssetCategoryRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
    private val transactionRepository: TransactionRepository,
) {
    fun getHome(userId: Long): GetHomeResponse {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)

        val range = user.getTransactionMonthlyRange(getNowKst())
        val daysLeft = getNowKst().untilInitializeTime(range.second, ChronoUnit.DAYS) + 1 // 오늘 포함
        val transactions = transactionRepository.findAllByTransactionDatetimeBetweenAndUserAndIsDeleted(
            transactionDatetimeStart = range.first, transactionDatetimeEnd = range.second, user = user, isDeleted = false)

        val assetCategories = assetCategoryRepository.findAllByUser(user)
        val assetCategoryDetails = assetCategories.map { category ->
            val totalIncome = transactions.filter { transaction ->
                transaction.assetCategory.id == category.id && transaction.incomeExpenseType == IncomeExpenseType.INCOME }
                .sumOf { it.price }
            val totalExpense = transactions.filter { transaction ->
                transaction.assetCategory.id == category.id && transaction.incomeExpenseType == IncomeExpenseType.EXPENSE }
                .sumOf { it.price }
            val budget = if (category.budgetPrice == null) null else (category.budgetPrice!! + totalIncome)
            val balance = if (budget == null) null else (budget - totalExpense)
            val availableExpensePricePerDay = if (balance == null) null else (balance / daysLeft)
            val planedExpensePerDay = if (budget == null) null
                else (budget / range.first.untilInitializeTime(range.second, ChronoUnit.DAYS))

            GetHomeResponse.AssetCategoryDetail(
                id = category.id,
                name = category.name,
                budget = budget,
                totalExpense = totalExpense,
                balance = balance,
                availableExpensePricePerDay = if (availableExpensePricePerDay != null && availableExpensePricePerDay < 0) 0
                    else availableExpensePricePerDay,
                isFine = if (planedExpensePerDay == null) null else (planedExpensePerDay <= availableExpensePricePerDay!!),
            )
        }

        val transactionCategories = transactionCategoryRepository.findAllByUser(user)
        val transactionCategoryDetails = transactionCategories.map { category ->
            val totalExpense = transactions.filter { transaction ->
                transaction.transactionCategory.id == category.id && transaction.incomeExpenseType == IncomeExpenseType.EXPENSE }
                .sumOf { it.price }

            GetHomeResponse.TransactionCategoryDetail(
                id = category.id,
                name = category.name,
                totalExpense = totalExpense,
            )
        }

        val totalIncome = transactions.filter { it.incomeExpenseType == IncomeExpenseType.INCOME }.sumOf { it.price }
        val totalExpense = transactions.filter { it.incomeExpenseType == IncomeExpenseType.EXPENSE }.sumOf { it.price }
        val hasBudget = assetCategories.any { it.budgetPrice != null }
        val totalBudget = if (hasBudget) assetCategories.filter { it.budgetPrice != null }.sumOf { it.budgetPrice!! } + totalIncome
            else null
        val totalBalance = if (totalBudget != null) (totalBudget - totalExpense) else null
        val availableExpensePricePerDay = if (totalBalance == null) null else (totalBalance / daysLeft)
        val planedExpensePerDay = if (totalBudget == null) null
            else (totalBudget / range.first.untilInitializeTime(range.second, ChronoUnit.DAYS))

        return GetHomeResponse(
            transactionMonthlyRange = user.getTransactionMonthlyRange(getNowKst()).toRange(),
            totalExpense = totalExpense,
            balance = totalBalance,
            availableExpensePricePerDay = if (availableExpensePricePerDay != null && availableExpensePricePerDay < 0) 0
                else availableExpensePricePerDay,
            isFine = if (planedExpensePerDay == null) null else (planedExpensePerDay <= availableExpensePricePerDay!!),
            assetCategories = assetCategoryDetails,
            transactionCategories = transactionCategoryDetails,
            wastedPrice = transactions.filter { it.isWaste && it.incomeExpenseType == IncomeExpenseType.EXPENSE }.sumOf { it.price },
        )
    }
}
