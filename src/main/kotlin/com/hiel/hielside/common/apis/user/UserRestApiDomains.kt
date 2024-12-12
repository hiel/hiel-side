package com.hiel.hielside.common.apis.user

import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.jpa.user.UserEntity
import com.hiel.hielside.common.utilities.isNotNullValidLengthTrimmed

data class UpdatePasswordRequest(
    val currentPassword: String,
    val updatePassword: String,
) {
    fun validate() {
        if (!updatePassword.isNotNullValidLengthTrimmed(min = UserEntity.PASSWORD_MINIMUM_LENGTH)) {
            throw ServiceException(
                resultCode = ResultCode.Auth.LENGTH_TOO_SHORT_PASSWORD,
                args = arrayOf(UserEntity.PASSWORD_MINIMUM_LENGTH),
            )
        }
    }
}
