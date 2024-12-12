package com.hiel.hielside.common.jpa.user

import com.hiel.hielside.common.domains.user.UserStatus
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findFirstByEmail(email: String): UserEntity?

    fun findFirstByIdAndUserStatus(id: Long, userStatus: UserStatus): UserEntity?

    fun findFirstByEmailAndUserStatus(email: String, userStatus: UserStatus): UserEntity?
}
