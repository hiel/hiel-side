package com.hiel.hielside.accountbook.jpa.user

import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.jpa.BaseEntity
import com.hiel.hielside.common.utilities.FIRST_DAY_OF_MONTH
import com.hiel.hielside.common.utilities.LAST_DAY_OF_MONTH
import com.hiel.hielside.common.utilities.getLastDayOfMonth
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import org.hibernate.envers.Audited
import java.time.OffsetDateTime

@Audited
@Entity
@Table(name = "users", catalog = "account_book")
class AccountBookUserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "email", updatable = false, nullable = false, length = 255)
    val email: String,

    @Column(name = "password", nullable = false, length = 255)
    var encryptPassword: String,

    @Column(name = "name", nullable = false, length = 20)
    val name: String,

    @Column(name = "user_type", updatable = false, nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    val userType: UserType,

    @Column(name = "user_status", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    var userStatus: UserStatus,

    @Column(name = "transaction_start_day", nullable = false)
    var transactionStartDay: Int,
) : BaseEntity() {
    companion object {
        const val PASSWORD_MINIMUM_LENGTH = 8
        const val USERNAME_MINIMUM_LENGTH = 2

        fun build(
            email: String,
            encryptPassword: String,
            name: String,
            userType: UserType,
            userStatus: UserStatus,
            transactionStartDay: Int = 1,
        ): AccountBookUserEntity {
            if (transactionStartDay !in FIRST_DAY_OF_MONTH..LAST_DAY_OF_MONTH) {
                throw ServiceException(ResultCode.User.INVALID_TRANSACTION_START_DAY)
            }
            return AccountBookUserEntity(
                email = email,
                encryptPassword = encryptPassword,
                name = name,
                userType = userType,
                userStatus = userStatus,
                transactionStartDay = transactionStartDay,
            )
        }
    }

    /**
     * startDay == 15
     * 2/20 -> 2/15 ~ 3/14
     * 2/15 -> 2/15 ~ 3/14
     *
     * startDay == 30
     * 24/3/20 -> 24/2/29(윤년) ~ 24/3/29
     */
    fun getTransactionMonthlyRange(datetime: OffsetDateTime): Pair<OffsetDateTime, OffsetDateTime> {
        var start = datetime
        var end = datetime.plusMonths(1)
        if (datetime.dayOfMonth != datetime.getLastDayOfMonth() && datetime.dayOfMonth < transactionStartDay) {
            start = start.minusMonths(1)
            end = end.minusMonths(1)
        }

        return (
            start.withDayOfMonth(if (start.getLastDayOfMonth() <= transactionStartDay) start.getLastDayOfMonth() else transactionStartDay)
        to
            end.withDayOfMonth(
                (if (end.getLastDayOfMonth() <= transactionStartDay) end.getLastDayOfMonth() else transactionStartDay)).minusDays(1)
        )
    }

    @PrePersist
    @PreUpdate
    fun prePersistUpdate() {
        if (transactionStartDay !in FIRST_DAY_OF_MONTH..LAST_DAY_OF_MONTH) {
            throw ServiceException(ResultCode.User.INVALID_TRANSACTION_START_DAY)
        }
    }
}
