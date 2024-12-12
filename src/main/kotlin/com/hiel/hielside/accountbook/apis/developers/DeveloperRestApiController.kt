package com.hiel.hielside.accountbook.apis.developers

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryEntity
import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transaction.TransactionEntity
import com.hiel.hielside.accountbook.jpa.transaction.TransactionRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.utilities.toOffsetDateTime
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/developers")
@RestController
class DeveloperRestApiController(
    private val userRepository: AccountBookUserRepository,
    private val budgetCategoryRepository: BudgetCategoryRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val transactionRepository: TransactionRepository,
) {
    @Transactional
    @PostMapping("/migrate")
    fun migrate() {
        val userCount = 5
        val budgetCategoryNames = listOf("국민은행", "현대카드", "비상금")
        val transactionCategoryNames = listOf("식비", "커피", "모임")
        val transactionCount = 30

        val userEntities = userRepository.saveAll(
            (1..userCount).map {
                AccountBookUserEntity(
                    email = "yangjunghooon$it@gmail.com",
                    encryptPassword = passwordEncoder.encode("yangjunghooon"),
                    name = "yangjunghooon$it",
                    userType = UserType.MASTER,
                    userStatus = UserStatus.AVAILABLE,
                )
            }
        )

        val budgetCategoryEntities = budgetCategoryRepository.saveAll(
            budgetCategoryNames.map {
                BudgetCategoryEntity(
                    name = it,
                    user = userEntities.first(),
                )
            }
        )

        val transactionCategoryEntities = transactionCategoryRepository.saveAll(
            transactionCategoryNames.map {
                TransactionCategoryEntity(
                    name = it,
                    user = userEntities.first(),
                )
            }
        )

        (1..transactionCount).forEach {
            transactionRepository.save(
                TransactionEntity(
                    incomeExpenseType = (IncomeExpenseType.entries + List(9) { IncomeExpenseType.EXPENSE }).random(),
                    title = "transaction$it",
                    price = listOf(10000, 12000, 1000, 500, 3000).random().toLong(),
                    isWaste = listOf(true, false).random(),
                    user = userEntities.first(),
                    budgetCategory = budgetCategoryEntities.random(),
                    transactionCategory = transactionCategoryEntities.random(),
                    transactionDatetime = "20240101".toOffsetDateTime("yyyyMMdd"),
                )
            )
        }
    }
}
