package com.hiel.hielside.accountbook.jpa.transactioncategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionCategoryRepository : JpaRepository<TransactionCategoryEntity, Long> {
    fun findFirstByIdAndUser(id: Long, user: AccountBookUserEntity): TransactionCategoryEntity?
    fun findFirstByIdAndUserAndIsDeleted(id: Long, user: AccountBookUserEntity, isDeleted: Boolean): TransactionCategoryEntity?
    fun findFirstByNameAndUser(name: String, user: AccountBookUserEntity): TransactionCategoryEntity?
    fun findAllByUser(user: AccountBookUserEntity): List<TransactionCategoryEntity>
    fun findAllByUserAndIsDeleted(user: AccountBookUserEntity, isDeleted: Boolean): List<TransactionCategoryEntity>
}
