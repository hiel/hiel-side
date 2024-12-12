package com.hiel.hielside.accountbook.apis.transactioncategory

import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TransactionCategoryService(
    private val userRepository: AccountBookUserRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository,
) {
    @Transactional
    fun register(
        name: String,
        userId: Long,
    ) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        transactionCategoryRepository.findFirstByNameAndUser(name = name, user = user)?.let {
            throw ServiceException(ResultCode.Common.EXIST_RESOURCE)
        }
        transactionCategoryRepository.save(
            TransactionCategoryEntity(
                name = name,
                user = user,
            )
        )
    }
}
