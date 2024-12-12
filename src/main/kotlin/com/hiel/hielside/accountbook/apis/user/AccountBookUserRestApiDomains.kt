package com.hiel.hielside.accountbook.apis.user

import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
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
