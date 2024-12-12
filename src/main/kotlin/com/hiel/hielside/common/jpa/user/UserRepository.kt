package com.hiel.hielside.common.jpa.user

import com.hiel.hielside.common.domains.ServiceType
import com.hiel.hielside.common.domains.user.UserStatus
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findFirstByEmailAndServiceType(email: String, serviceType: ServiceType): UserEntity?

    fun findFirstByIdAndUserStatus(id: Long, userStatus: UserStatus): UserEntity?

    fun findFirstByEmailAndUserStatusAndServiceType(email: String, userStatus: UserStatus, serviceType: ServiceType): UserEntity?
    fun name(name: String): MutableList<UserEntity>
}
