package com.hiel.hielside.common.jpa.user

import com.hiel.hielside.common.domains.ServiceType
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.envers.Audited

@Audited
@Entity
@Table(name = "users", schema = "hiel_side")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "service_type", updatable = false, nullable = false)
    @Enumerated(value = EnumType.STRING)
    val serviceType: ServiceType,

    @Column(name = "email", updatable = false, nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    var encryptPassword: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "user_type", updatable = false, nullable = false)
    @Enumerated(value = EnumType.STRING)
    val userType: UserType,

    @Column(name = "user_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    var userStatus: UserStatus,
) : BaseEntity() {
    companion object {
        const val PASSWORD_MINIMUM_LENGTH = 8
        const val USERNAME_MINIMUM_LENGTH = 2
    }
}
