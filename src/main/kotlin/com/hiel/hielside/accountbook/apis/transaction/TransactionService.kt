package com.hiel.hielside.accountbook.apis.transaction

import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryRepository
import com.hiel.hielside.accountbook.jpa.transaction.TransactionEntity
import com.hiel.hielside.accountbook.jpa.transaction.TransactionRepository
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
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
    private val assetCategoryRepository: AssetCategoryRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
) {
    @Transactional
    fun register(request: RegisterTransactionRequest, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val assetCategory = assetCategoryRepository.findByIdOrNull(request.assetCategoryId)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        val transactionCategory = transactionCategoryRepository.findByIdOrNull(request.transactionCategoryId)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)

        transactionRepository.save(
            request.build(
                user = user,
                assetCategory = assetCategory,
                transactionCategory = transactionCategory,
            )
        )
    }

    @Transactional
    fun update(transactionId: Long, request: UpdateTransactionRequest, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val transaction = transactionRepository.findFirstByIdAndUserAndIsDeleted(id = transactionId, user = user, isDeleted = false)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)

        transaction.incomeExpenseType = request.incomeExpenseType
        transaction.title = request.title
        transaction.price = request.price
        transaction.isWaste = request.isWaste
        transaction.assetCategory = assetCategoryRepository.findByIdOrNull(request.assetCategoryId)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        transaction.transactionCategory = transactionCategoryRepository.findByIdOrNull(request.transactionCategoryId)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        transaction.transactionDatetime = request.transactionDate
    }

    @Transactional
    fun delete(transactionId: Long, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val transaction = transactionRepository.findFirstByIdAndUser(id = transactionId, user = user)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)

        transaction.delete(userId)
    }

    fun getDetail(transactionId: Long, userId: Long): TransactionEntity {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return transactionRepository.findFirstByIdAndUserAndIsDeleted(id = transactionId, user = user, isDeleted = false)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
    }

    fun getSlice(
        transactionDatetime: OffsetDateTime,
        page: Int,
        pageSize: Int,
        userId: Long,
    ): Slice<TransactionEntity> {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return transactionRepository.findAllByTransactionDatetimeBetweenAndUserAndIsDeletedOrderByTransactionDatetimeDesc(
            transactionDatetimeStart = transactionDatetime.convertToFirstDayOfMonth(),
            transactionDatetimeEnd = transactionDatetime.convertToLastDayOfMonth(),
            user = user,
            isDeleted = false,
            pageable = pageOf(page, pageSize),
        )
    }
}
