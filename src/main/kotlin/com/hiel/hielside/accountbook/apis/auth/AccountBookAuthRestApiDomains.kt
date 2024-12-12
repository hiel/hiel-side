package com.hiel.hielside.accountbook.apis.auth

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.domains.auth.AuthToken
import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.exceptions.ServiceException
import com.hiel.hielside.common.utilities.Regex
import com.hiel.hielside.common.utilities.isNotNullValidLengthTrimmed
import com.hiel.hielside.common.utilities.regexMatches

data class AccountBookSignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val userType: UserType,
) {
    fun validate() {
        if (!email.regexMatches(Regex.EMAIL)) {
            throw ServiceException(ResultCode.Auth.INVALID_FORMAT_EMAIL)
        }
        if (!password.isNotNullValidLengthTrimmed(min = AccountBookUserEntity.PASSWORD_MINIMUM_LENGTH)) {
            throw ServiceException(
                resultCode = ResultCode.Auth.LENGTH_TOO_SHORT_PASSWORD,
                args = arrayOf(AccountBookUserEntity.PASSWORD_MINIMUM_LENGTH),
            )
        }
        if (!name.isNotNullValidLengthTrimmed(min = AccountBookUserEntity.USERNAME_MINIMUM_LENGTH)) {
            throw ServiceException(
                resultCode = ResultCode.Auth.LENGTH_TOO_SHORT_NAME,
                args = arrayOf(AccountBookUserEntity.USERNAME_MINIMUM_LENGTH),
            )
        }
    }
}

data class AccountBookCertificateSignupRequest(
    val signupToken: String,
)

data class AccountBookLoginRequest(
    val email: String,
    val password: String,
)

data class AccountBookIssueTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val id: Long,
    val email: String,
    val name: String,
    val userType: UserType,
) {
    companion object {
        fun build(authToken: AuthToken, userEntity: AccountBookUserEntity) = AccountBookIssueTokenResponse(
            accessToken = authToken.accessToken,
            refreshToken = authToken.refreshToken,
            id = userEntity.id,
            email = userEntity.email,
            name = userEntity.name,
            userType = userEntity.userType,
        )
    }
}

data class AccountBookRefreshTokenRequest(
    val refreshToken: String,
)

data class AccountBookRequestPasswordResetRequest(
    val email: String,
)

data class AccountBookResetPasswordRequest(
    val resetPasswordToken: String,
)

data class AccountBookResetPasswordResponse(
    val password: String,
)
