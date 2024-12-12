package com.hiel.hielside.accountbook.jpa.budgetcategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BudgetCategoryRepository : JpaRepository<BudgetCategoryEntity, Long> {
    fun findFirstByNameAndUser(name: String, user: AccountBookUserEntity): BudgetCategoryEntity?
}
