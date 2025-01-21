package com.hiel.hielside.accountbook.apis.transactioncategory

import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TransactionCategoryService(
    private val userRepository: AccountBookUserRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
) {
    @Transactional
    fun register(name: String, budgetPrice: Long?, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        transactionCategoryRepository.findFirstByNameAndUser(name = name, user = user)?.let {
            throw ServiceException(ResultCode.Common.EXIST_RESOURCE) }
        transactionCategoryRepository.save(TransactionCategoryEntity(name = name, budgetPrice = budgetPrice, user = user))
    }

    @Transactional
    fun update(transactionCategoryId: Long, name: String, budgetPrice: Long?, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val transactionCategory = transactionCategoryRepository.findFirstByIdAndUserAndIsDeleted(
            id = transactionCategoryId, user = user, isDeleted = false)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        transactionCategory.name = name
        transactionCategory.budgetPrice = budgetPrice
    }

    @Transactional
    fun delete(transactionCategoryId: Long, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val transactionCategory = transactionCategoryRepository.findFirstByIdAndUser(id = transactionCategoryId, user = user)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        transactionCategory.delete(userId)
    }

    fun getAll(userId: Long): List<TransactionCategoryEntity> {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return transactionCategoryRepository.findAllByUserAndIsDeleted(user = user, isDeleted = false)
    }
}
