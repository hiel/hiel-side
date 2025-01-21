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
import com.hiel.hielside.common.utilities.getNowKst
import com.hiel.hielside.common.utilities.toFormatString
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
        val transactionCount = 10

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
            val datetime = getNowKst()
                .plusMonths((-2..2).random().toLong())
                .withDayOfMonth((1..28).random())
                .withHour((0..23).random())
                .withMinute((0..59).random())
                .withSecond((0..59).random())
            val incomeExpenseType = (IncomeExpenseType.entries + List(9) { IncomeExpenseType.EXPENSE }).random()
            transactionRepository.save(
                TransactionEntity(
                    incomeExpenseType = incomeExpenseType,
                    title = "내역${datetime.toFormatString("yyMMdd")}",
                    price = ((100..100000).random() / 10 * 10).toLong(),
                    isWaste = if (incomeExpenseType == IncomeExpenseType.INCOME) false else listOf(true, false).random(),
                    user = userEntities.first(),
                    budgetCategory = budgetCategoryEntities.random(),
                    transactionCategory = transactionCategoryEntities.random(),
                    transactionDatetime = datetime,
                )
            )
        }
    }
}
