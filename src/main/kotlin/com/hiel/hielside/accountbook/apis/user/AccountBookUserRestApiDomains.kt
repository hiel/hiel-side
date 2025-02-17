package com.hiel.hielside.accountbook.apis.user

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.utilities.FIRST_DAY_OF_MONTH
import com.hiel.hielside.common.utilities.LAST_DAY_OF_MONTH
import com.hiel.hielside.common.utilities.isNotNullValidLengthTrimmed

data class UpdatePasswordRequest(
    val currentPassword: String,
    val updatePassword: String,
) {
    fun validate() {
        if (!updatePassword.isNotNullValidLengthTrimmed(min = AccountBookUserEntity.PASSWORD_MINIMUM_LENGTH)) {
            throw ServiceException(
                resultCode = ResultCode.Auth.LENGTH_TOO_SHORT_PASSWORD,
                args = arrayOf(AccountBookUserEntity.PASSWORD_MINIMUM_LENGTH),
            )
        }
    }
}

data class UpdateTransactionStartDayRequest(
    val transactionStartDay: Int,
) {
    fun validate() {
        if (transactionStartDay !in FIRST_DAY_OF_MONTH..LAST_DAY_OF_MONTH) {
            throw ServiceException(ResultCode.User.INVALID_TRANSACTION_START_DAY)
        }
    }
}

data class UpdateTransactionStartDayResponse(
    val transactionStartDay: Int,
)

data class GetUserResponse(
    val transactionStartDay: Int,
) {
    companion object {
        fun build(user: AccountBookUserEntity) = GetUserResponse(
            transactionStartDay = user.transactionStartDay,
        )
    }
}
