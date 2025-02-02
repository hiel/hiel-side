package com.hiel.hielside.accountbook.jpa.assetcategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AssetCategoryRepository : JpaRepository<AssetCategoryEntity, Long> {
    fun findFirstByIdAndUser(id: Long, user: AccountBookUserEntity): AssetCategoryEntity?
    fun findFirstByIdAndUserAndIsActive(id: Long, user: AccountBookUserEntity, isActive: Boolean): AssetCategoryEntity?
    fun findFirstByNameAndUser(name: String, user: AccountBookUserEntity): AssetCategoryEntity?
    fun findAllByUser(user: AccountBookUserEntity): List<AssetCategoryEntity>
    fun findAllByUserAndIsActive(user: AccountBookUserEntity, isActive: Boolean): List<AssetCategoryEntity>

}
