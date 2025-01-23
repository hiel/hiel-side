package com.hiel.hielside.accountbook.apis.developers

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
import com.hiel.hielside.common.utilities.DateTimeFormat
import com.hiel.hielside.common.utilities.FIRST_DAY_OF_MONTH
import com.hiel.hielside.common.utilities.LAST_DAY_OF_MONTH
import com.hiel.hielside.common.utilities.getNowKst
import com.hiel.hielside.common.utilities.toFormatString
import com.hiel.hielside.common.utilities.truncate
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/developers")
@RestController
class DeveloperRestApiController(
    private val userRepository: AccountBookUserRepository,
    private val assetCategoryRepository: AssetCategoryRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val transactionRepository: TransactionRepository,
) {
    @Transactional
    @PostMapping("/migrate")
    fun migrate() {
        val userCount = 5
        val assetCategoryNames = listOf("국민은행", "현대카드", "비상금")
        val transactionCategoryNames = listOf("식비", "커피", "모임")
        val transactionCount = 10

        val userEntities = userRepository.saveAll(
            (1..userCount).map {
                AccountBookUserEntity.build(
                    email = "yangjunghooon$it@gmail.com",
                    encryptPassword = passwordEncoder.encode("yangjunghooon"),
                    name = "yangjunghooon$it",
                    userType = UserType.MASTER,
                    userStatus = UserStatus.AVAILABLE,
                    transactionStartDay = (FIRST_DAY_OF_MONTH..LAST_DAY_OF_MONTH).random(),
                )
            }
        )

        val assetCategoryEntities = assetCategoryRepository.saveAll(
            assetCategoryNames.map {
                AssetCategoryEntity(
                    name = it,
                    budgetPrice = listOf(((0..10000000).random()).truncate(3).toLong(), null).random(),
                    user = userEntities.first(),
                )
            }
        )

        val transactionCategoryEntities = transactionCategoryRepository.saveAll(
            transactionCategoryNames.map {
                TransactionCategoryEntity(
                    name = it,
                    budgetPrice = listOf(((0..10000000).random()).truncate(3).toLong(), null).random(),
                    user = userEntities.first(),
                )
            }
        )

        (1..transactionCount).forEach { _ ->
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
                    title = "내역${datetime.toFormatString(DateTimeFormat.DATE_SHORT_YEAR)}",
                    price = ((100..100000).random().truncate(2)).toLong(),
                    isWaste = if (incomeExpenseType == IncomeExpenseType.INCOME) false else listOf(true, false).random(),
                    user = userEntities.first(),
                    assetCategory = assetCategoryEntities.random(),
                    transactionCategory = transactionCategoryEntities.random(),
                    transactionDatetime = datetime,
                )
            )
        }
    }
}
