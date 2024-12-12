package com.hiel.hielside.accountbook.jpa.budgetcategory

import com.hiel.hielside.common.jpa.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BudgetCategoryRepository : JpaRepository<BudgetCategoryEntity, Long> {
    fun findFirstByNameAndUser(name: String, user: UserEntity): BudgetCategoryEntity?
}
