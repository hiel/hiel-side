package com.hiel.hielside.accountbook.apis.assetcategory

import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryEntity
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryRepository
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AssetCategoryService(
    private val userRepository: AccountBookUserRepository,
    private val assetCategoryRepository: AssetCategoryRepository,
) {
    @Transactional
    fun register(name: String, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        assetCategoryRepository.findFirstByNameAndUser(name = name, user = user)?.let {
            throw ServiceException(ResultCode.Common.EXIST_RESOURCE) }
        assetCategoryRepository.save(AssetCategoryEntity(name = name, user = user))
    }

    @Transactional
    fun update(assetCategoryId: Long, name: String, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val assetCategory = assetCategoryRepository.findFirstByIdAndUserAndIsDeleted(
            id = assetCategoryId, user = user, isDeleted = false)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        assetCategory.name = name
    }

    @Transactional
    fun delete(assetCategoryId: Long, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        val assetCategory = assetCategoryRepository.findFirstByIdAndUser(id = assetCategoryId, user = user)
            ?: throw ServiceException(ResultCode.Common.NOT_EXIST_RESOURCE)
        assetCategory.delete(userId)
    }

    fun getAll(userId: Long): List<AssetCategoryEntity> {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        return assetCategoryRepository.findAllByUserAndIsDeleted(user = user, isDeleted = false)
    }
}
