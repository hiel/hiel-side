package com.hiel.hielside.accountbook.apis.home

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transaction.TransactionRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.utilities.getNowKst
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit

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
        val daysLeft = getNowKst().until(range.second, ChronoUnit.DAYS) + 1 // 오늘 포함
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
            val balance = if (category.budgetPrice == null) null else (category.budgetPrice!! + totalIncome - totalExpense)
            val availableExpensePricePerDay = if (balance == null) null else (balance / daysLeft)
            val planedExpensePerDay = if (category.budgetPrice == null) null
                else (category.budgetPrice!! / range.first.until(range.second, ChronoUnit.DAYS))

            GetHomeResponse.AssetCategoryDetail(
                id = category.id,
                name = category.name,
                budget = category.budgetPrice,
                totalExpense = totalExpense,
                balance = balance,
                availableExpensePricePerDay = availableExpensePricePerDay,
                isFine = if (planedExpensePerDay == null) null else (planedExpensePerDay <= availableExpensePricePerDay!!),
            )
        }

        val transactionCategories = transactionCategoryRepository.findAllByUser(user)
        val transactionCategoryDetails = transactionCategories.map { category ->
            val totalIncome = transactions.filter { transaction ->
                transaction.transactionCategory.id == category.id && transaction.incomeExpenseType == IncomeExpenseType.INCOME }
                .sumOf { it.price }
            val totalExpense = transactions.filter { transaction ->
                transaction.transactionCategory.id == category.id && transaction.incomeExpenseType == IncomeExpenseType.EXPENSE }
                .sumOf { it.price }
            val balance = if (category.budgetPrice == null) null else (category.budgetPrice!! + totalIncome - totalExpense)
            val availableExpensePricePerDay = if (balance == null) null else (balance / daysLeft)
            val planedExpensePerDay = if (category.budgetPrice == null) null
                else (category.budgetPrice!! / range.first.until(range.second, ChronoUnit.DAYS))

            GetHomeResponse.TransactionCategoryDetail(
                id = category.id,
                name = category.name,
                budget = category.budgetPrice,
                totalExpense = totalExpense,
                balance = balance,
                availableExpensePricePerDay = availableExpensePricePerDay,
                isFine = if (planedExpensePerDay == null) null else (planedExpensePerDay <= availableExpensePricePerDay!!),
            )
        }

        val hasBudget = (assetCategories.filter { it.budgetPrice != null } + transactionCategories.filter { it.budgetPrice != null })
            .isNotEmpty()
        val totalBudget = if (hasBudget) assetCategories.filter { it.budgetPrice != null }.sumOf { it.budgetPrice!! } +
            transactionCategories.filter { it.budgetPrice != null }.sumOf { it.budgetPrice!! } else null
        val totalExpense = transactions.filter { it.incomeExpenseType == IncomeExpenseType.EXPENSE }.sumOf { it.price }
        val totalBalance = if (totalBudget != null) (totalBudget - totalExpense) else null
        val availableExpensePricePerDay = if (totalBalance == null) null else (totalBalance / daysLeft)
        val planedExpensePerDay = if (totalBudget == null) null
            else (totalBudget / range.first.until(range.second, ChronoUnit.DAYS))

        return GetHomeResponse(
            budget = totalBudget,
            totalExpense = totalExpense,
            balance = totalBalance,
            availableExpensePricePerDay = availableExpensePricePerDay,
            isFine = if (planedExpensePerDay == null) null else (planedExpensePerDay <= availableExpensePricePerDay!!),
            assetCategories = assetCategoryDetails,
            transactionCategories = transactionCategoryDetails,
            wastedPrice = transactions.filter { it.isWaste && it.incomeExpenseType == IncomeExpenseType.EXPENSE }.sumOf { it.price },
        )
    }
}
