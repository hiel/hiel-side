package com.hiel.hielside.common.apis.user

import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.jpa.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun updatePassword(currentPassword: String, updatePassword: String, userId: Long) {
        val user = userRepository.findFirstByIdAndUserStatus(id = userId, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
        if (!passwordEncoder.matches(currentPassword, user.encryptPassword)) {
            throw ServiceException(ResultCode.Auth.INVALID_PASSWORD)
        }
        user.encryptPassword = passwordEncoder.encode(updatePassword)
    }
}
