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
    fun register(name: String, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val category = transactionCategoryRepository.findFirstByNameAndUser(name = name, user = user)
        if (category == null) {
            transactionCategoryRepository.save(TransactionCategoryEntity(name = name, user = user))
        } else if (!category.isActive) {
            category.isActive = true
        } else {
            throw ServiceException(ResultCode.Common.EXIST_RESOURCE)
        }
    }

    @Transactional
    fun update(transactionCategoryId: Long, name: String, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val transactionCategory = transactionCategoryRepository.findFirstByIdAndUserAndIsActive(
            id = transactionCategoryId, user = user, isActive = true,
        )
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        transactionCategory.name = name
    }

    @Transactional
    fun deactivate(transactionCategoryId: Long, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val transactionCategory = transactionCategoryRepository.findFirstByIdAndUser(id = transactionCategoryId, user = user)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        transactionCategory.isActive = false
    }

    fun getAll(userId: Long): List<TransactionCategoryEntity> {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return transactionCategoryRepository.findAllByUserAndIsActive(user = user, isActive = true)
    }
}
