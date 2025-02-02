package com.hiel.hielside.accountbook.apis.home

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryEntity
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transaction.TransactionEntity
import com.hiel.hielside.accountbook.jpa.transaction.TransactionRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.utilities.getNowKst
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import java.time.OffsetDateTime
import java.time.ZoneOffset

class AccountBookHomeServiceTest : FunSpec ({
    val userRepository = mockk<AccountBookUserRepository>()
    val assetCategoryRepository = mockk<AssetCategoryRepository>()
    val transactionCategoryRepository = mockk<TransactionCategoryRepository>()
    val transactionRepository = mockk<TransactionRepository>()

    val service = AccountBookHomeService(
        userRepository,
        assetCategoryRepository,
        transactionCategoryRepository,
        transactionRepository
    )

    context("getHome") {
        test("getHome") {
            mockkStatic("com.hiel.hielside.common.utilities.DateTimeUtilityKt")

            val user = spyk(AccountBookUserEntity(
                id = 1,
                email = "TEST@TEST.com",
                encryptPassword = "TEST_ENCRYPT_PASSWORD",
                name = "TEST_NAME",
                userType = UserType.entries.random(),
                userStatus = UserStatus.entries.random(),
                transactionStartDay = 1,
            ))
            val assetCategories = listOf(
                AssetCategoryEntity(id = 1, name = "TEST_ASSET_CATEGORY-1", budgetPrice = 100000, user = user),
                AssetCategoryEntity(id = 2, name = "TEST_ASSET_CATEGORY-2", budgetPrice = 10000, user = user),
                AssetCategoryEntity(id = 3, name = "TEST_ASSET_CATEGORY-3", budgetPrice = 5000, user = user),
            )
            val transactionCategories = listOf(
                TransactionCategoryEntity(id = 1, name = "TEST_TRANSACTION_CATEGORY-1", user = user),
                TransactionCategoryEntity(id = 2, name = "TEST_TRANSACTION_CATEGORY-2", user = user),
                TransactionCategoryEntity(id = 3, name = "TEST_TRANSACTION_CATEGORY-3", user = user),
            )
            val transactions = listOf(
                TransactionEntity(
                    id = 1,
                    incomeExpenseType = IncomeExpenseType.EXPENSE,
                    title = "TEST_TITLE",
                    price = 20000,
                    isWaste = false,
                    user = user,
                    assetCategory = assetCategories[0],
                    transactionCategory = transactionCategories[1],
                    transactionDatetime = OffsetDateTime.of(2025, 2, 8, 0, 0, 0, 0, ZoneOffset.UTC),
                ),
                TransactionEntity(
                    id = 1,
                    incomeExpenseType = IncomeExpenseType.EXPENSE,
                    title = "TEST_TITLE",
                    price = 1000,
                    isWaste = false,
                    user = user,
                    assetCategory = assetCategories[2],
                    transactionCategory = transactionCategories[2],
                    transactionDatetime = OffsetDateTime.of(2025, 2, 8, 0, 0, 0, 0, ZoneOffset.UTC),
                ),
                TransactionEntity(
                    id = 1,
                    incomeExpenseType = IncomeExpenseType.INCOME,
                    title = "TEST_TITLE",
                    price = 1000,
                    isWaste = false,
                    user = user,
                    assetCategory = assetCategories[0],
                    transactionCategory = transactionCategories[1],
                    transactionDatetime = OffsetDateTime.of(2025, 2, 8, 0, 0, 0, 0, ZoneOffset.UTC),
                ),
            )

            every { getNowKst() } returns
                OffsetDateTime.of(2025, 2, 9, 0, 0, 0, 0, ZoneOffset.UTC)

            every { user.getTransactionMonthlyRange(any()) } returns (
                OffsetDateTime.of(2025, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                    to OffsetDateTime.of(2025, 2, 28, 0, 0, 0, 0, ZoneOffset.UTC))

            every { userRepository.findFirstByIdAndUserStatus(any(), any()) } returns user
            every { transactionRepository.findAllByTransactionDatetimeBetweenAndUserAndIsDeleted(any(), any(), any(), any()) } returns
                transactions
            every { assetCategoryRepository.findAllByUser(any()) } returns assetCategories
            every { transactionCategoryRepository.findAllByUser(any()) } returns transactionCategories

            val result = service.getHome(userId = 1)

            var totalIncome = 1000
            var totalExpense = 20000 + 1000
            var budget = (100000 + 10000 + 5000) + totalIncome
            var balance = budget - totalExpense
            var availableExpensePricePerDay = balance / 20
            result.totalExpense shouldBe totalExpense
            result.balance shouldBe balance
            result.availableExpensePricePerDay shouldBe availableExpensePricePerDay
            result.isFine shouldBe ((budget / 28) <= availableExpensePricePerDay)

            result.assetCategories.size shouldBe 3

            totalIncome = 1000
            totalExpense = 20000
            budget = 100000 + totalIncome
            balance = budget - totalExpense
            availableExpensePricePerDay = balance / 20
            result.assetCategories[0].budget shouldBe budget
            result.assetCategories[0].totalExpense shouldBe totalExpense
            result.assetCategories[0].balance shouldBe balance
            result.assetCategories[0].availableExpensePricePerDay shouldBe availableExpensePricePerDay
            result.assetCategories[0].isFine shouldBe ((budget / 28) <= availableExpensePricePerDay)

            result.assetCategories[1].totalExpense shouldBe 0
            result.assetCategories[1].isFine shouldBe true

            result.transactionCategories.size shouldBe 3
            result.transactionCategories[1].totalExpense shouldBe 20000

            result.wastedPrice shouldBe 0
        }
    }
})
