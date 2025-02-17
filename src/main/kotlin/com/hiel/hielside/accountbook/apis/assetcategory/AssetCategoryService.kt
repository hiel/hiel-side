package com.hiel.hielside.accountbook.apis.assetcategory

import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryEntity
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryRepository
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AssetCategoryService(
    private val userRepository: AccountBookUserRepository,
    private val assetCategoryRepository: AssetCategoryRepository,
) {
    @Transactional
    fun register(name: String, budgetPrice: Long?, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val category = assetCategoryRepository.findFirstByNameAndUser(name = name, user = user)
        if (category == null) {
            assetCategoryRepository.save(AssetCategoryEntity(name = name, budgetPrice = budgetPrice, user = user))
        } else if (!category.isActive) {
            category.isActive = true
            category.budgetPrice = budgetPrice
        } else {
            throw ServiceException(ResultCode.Common.EXIST_RESOURCE)
        }
    }

    @Transactional
    fun update(assetCategoryId: Long, name: String, budgetPrice: Long?, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val assetCategory = assetCategoryRepository.findFirstByIdAndUserAndIsActive(
            id = assetCategoryId, user = user, isActive = true,
        )
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        assetCategory.name = name
        assetCategory.budgetPrice = budgetPrice
    }

    @Transactional
    fun deactivate(assetCategoryId: Long, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val assetCategory = assetCategoryRepository.findFirstByIdAndUser(id = assetCategoryId, user = user)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        assetCategory.isActive = false
    }

    fun getAll(userId: Long): List<AssetCategoryEntity> {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return assetCategoryRepository.findAllByUserAndIsActive(user = user, isActive = true)
    }
}
