package com.hiel.hielside.accountbook.jpa.user

import com.hiel.hielside.common.domains.user.UserStatus
import org.springframework.data.jpa.repository.JpaRepository

interface AccountBookUserRepository : JpaRepository<AccountBookUserEntity, Long> {
    fun findFirstByEmail(email: String): AccountBookUserEntity?

    fun findFirstByIdAndUserStatus(id: Long, userStatus: UserStatus): AccountBookUserEntity?

    fun findFirstByEmailAndUserStatus(email: String, userStatus: UserStatus): AccountBookUserEntity?
    fun name(name: String): MutableList<AccountBookUserEntity>
}
