package com.hiel.hielside.accountbook.jpa.assetcategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AssetCategoryRepository : JpaRepository<AssetCategoryEntity, Long> {
    fun findFirstByIdAndUser(id: Long, user: AccountBookUserEntity): AssetCategoryEntity?
    fun findFirstByIdAndUserAndIsDeleted(id: Long, user: AccountBookUserEntity, isDeleted: Boolean): AssetCategoryEntity?
    fun findFirstByNameAndUser(name: String, user: AccountBookUserEntity): AssetCategoryEntity?
    fun findAllByUserAndIsDeleted(user: AccountBookUserEntity, isDeleted: Boolean): List<AssetCategoryEntity>

}
