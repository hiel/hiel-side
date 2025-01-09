package com.hiel.hielside.accountbook.jpa.budgetcategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BudgetCategoryRepository : JpaRepository<BudgetCategoryEntity, Long> {
    fun findFirstByIdAndUser(id: Long, user: AccountBookUserEntity): BudgetCategoryEntity?
    fun findFirstByIdAndUserAndIsDeleted(id: Long, user: AccountBookUserEntity, isDeleted: Boolean): BudgetCategoryEntity?
    fun findFirstByNameAndUser(name: String, user: AccountBookUserEntity): BudgetCategoryEntity?
    fun findAllByUserAndIsDeleted(user: AccountBookUserEntity, isDeleted: Boolean): List<BudgetCategoryEntity>

}
