package com.hiel.hielside.accountbook.jpa.transactioncategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionCategoryRepository : JpaRepository<TransactionCategoryEntity, Long> {
    fun findAllByUser(user: AccountBookUserEntity): List<TransactionCategoryEntity>
    fun findFirstByNameAndUser(name: String, user: AccountBookUserEntity): TransactionCategoryEntity?
}
