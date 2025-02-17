package com.hiel.hielside.accountbook.jpa.transactioncategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionCategoryRepository : JpaRepository<TransactionCategoryEntity, Long> {
    fun findFirstByIdAndUser(id: Long, user: AccountBookUserEntity): TransactionCategoryEntity?

    fun findFirstByIdAndUserAndIsActive(id: Long, user: AccountBookUserEntity, isActive: Boolean): TransactionCategoryEntity?

    fun findFirstByNameAndUser(name: String, user: AccountBookUserEntity): TransactionCategoryEntity?

    fun findAllByUser(user: AccountBookUserEntity): List<TransactionCategoryEntity>

    fun findAllByUserAndIsActive(user: AccountBookUserEntity, isActive: Boolean): List<TransactionCategoryEntity>
}
