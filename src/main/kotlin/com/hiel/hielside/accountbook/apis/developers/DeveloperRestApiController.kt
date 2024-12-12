package com.hiel.hielside.accountbook.apis.developers

import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryEntity
import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.common.domains.ServiceType
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.jpa.user.UserEntity
import com.hiel.hielside.common.jpa.user.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/account-book/developers")
@RestController
class DeveloperRestApiController(
    private val userRepository: UserRepository,
    private val budgetCategoryRepository: BudgetCategoryRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    @Transactional
    @PostMapping("/migrate")
    fun migrate() {
        userRepository.saveAll(
            (1..5).map {
                UserEntity(
                    serviceType = ServiceType.ACCOUNT_BOOK,
                    email = "yangjunghooon$it@gmail.com",
                    encryptPassword = passwordEncoder.encode("yangjunghooon"),
                    name = "yangjunghooon$it",
                    userType = UserType.MASTER,
                    userStatus = UserStatus.AVAILABLE,
                )
            }
        )

        budgetCategoryRepository.saveAll(
            listOf("국민은행", "현대카드", "비상금").map {
                BudgetCategoryEntity(
                    name = it,
                    user = userRepository.findById(1).orElseThrow(),
                )
            }
        )

        transactionCategoryRepository.saveAll(
            listOf("식비", "커피", "모임").map {
                TransactionCategoryEntity(
                    name = it,
                    user = userRepository.findById(1).orElseThrow(),
                )
            }
        )
    }
}
