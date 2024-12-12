package com.hiel.hielside.accountbook.jpa.transactioncategory

import com.hiel.hielside.common.jpa.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionCategoryRepository : JpaRepository<TransactionCategoryEntity, Long> {
    fun findFirstByNameAndUser(name: String, user: UserEntity): TransactionCategoryEntity?
}
