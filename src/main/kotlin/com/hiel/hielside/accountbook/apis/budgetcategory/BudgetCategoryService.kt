package com.hiel.hielside.accountbook.apis.budgetcategory

import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryEntity
import com.hiel.hielside.accountbook.jpa.budgetcategory.BudgetCategoryRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BudgetCategoryService(
    private val userRepository: AccountBookUserRepository,
    private val budgetCategoryRepository: BudgetCategoryRepository,
) {
    @Transactional
    fun register(name: String, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        budgetCategoryRepository.findFirstByNameAndUser(name = name, user = user)?.let {
            throw ServiceException(ResultCode.Common.EXIST_RESOURCE) }
        budgetCategoryRepository.save(BudgetCategoryEntity(name = name, user = user))
    }

    @Transactional
    fun update(budgetCategoryId: Long, name: String, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val budgetCategory = budgetCategoryRepository.findFirstByIdAndUserAndIsDeleted(
            id = budgetCategoryId, user = user, isDeleted = false)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        budgetCategory.name = name
    }

    @Transactional
    fun delete(budgetCategoryId: Long, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val budgetCategory = budgetCategoryRepository.findFirstByIdAndUser(id = budgetCategoryId, user = user)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        budgetCategory.delete(userId)
    }

    fun getAll(userId: Long): List<BudgetCategoryEntity> {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return budgetCategoryRepository.findAllByUserAndIsDeleted(user = user, isDeleted = false)
    }
}
