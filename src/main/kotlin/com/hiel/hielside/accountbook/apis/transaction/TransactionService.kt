package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transaction.TransactionEntity
import com.hiel.hielside.accountbook.jpa.transaction.TransactionRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.utilities.convertToFirstDayOfMonth
import com.hiel.hielside.common.utilities.convertToLastDayOfMonth
import com.hiel.hielside.common.utilities.pageOf
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class TransactionService(
    private val userRepository: AccountBookUserRepository,
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

    fun getSlice(
        transactionDatetime: OffsetDateTime,
        page: Int,
        pageSize: Int,
        userId: Long,
    ): Slice<TransactionEntity> {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return transactionRepository.findAllByTransactionDatetimeBetweenAndUserOrderByTransactionDatetimeDesc(
            transactionDatetimeStart = transactionDatetime.convertToFirstDayOfMonth(),
            transactionDatetimeEnd = transactionDatetime.convertToLastDayOfMonth(),
            user = user,
            pageable = pageOf(page, pageSize),
        )
    }
}
