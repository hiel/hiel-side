package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transaction.TransactionRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.jpa.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val budgetCategoryRepository: BudgetCategoryRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
) {
    @Transactional
    fun register(
        request: RegisterTransactionRequest,
        userId: Long,
    ) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val budgetCategory = budgetCategoryRepository.findByIdOrNull(request.budgetCategoryId)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        val transactionCategory = transactionCategoryRepository.findByIdOrNull(request.transactionCategoryId)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)

        transactionRepository.save(
            request.build(
                user = user,
                budgetCategory = budgetCategory,
                transactionCategory = transactionCategory,
            )
        )
    }
}
