package com.hiel.hielside.common.utilities.mail

interface MailTemplateParams

sealed class MailTemplate<T : MailTemplateParams>(
    open val params: T? = null,
) {
    protected abstract fun subject(): String

    protected abstract fun text(): String

    fun getSubject() = subject().trimIndent()

    fun getText() = text().trimIndent()

    class SignupCertificate(
        override val params: Params,
    ) : MailTemplate<SignupCertificate.Params>(params) {
        data class Params(
            val webClientUrl: String,
            val token: String,
        ) : MailTemplateParams

        override fun subject(): String =
            "가입 인증 메일"

        override fun text(): String = with(params) {
            """
            $webClientUrl/auth/signup/certificate?token=$token
            """
        }
    }

    class InviteUser(
        override val params: Params,
    ) : MailTemplate<InviteUser.Params>(params) {
        data class Params(
            val expirationDate: Int,
            val webClientUrl: String,
            val token: String,
        ) : MailTemplateParams

        override fun subject(): String =
            "Invite User"

        override fun text(): String = with(params) {
            """
            expirationDate : $expirationDate Days
            $webClientUrl/auth/signup?token=$token
            """
        }
    }

    class PasswordReset(
        override val params: Params,
    ) : MailTemplate<PasswordReset.Params>(params) {
        data class Params(
            val webClientUrl: String,
            val token: String,
        ) : MailTemplateParams

        override fun subject(): String =
            "Reset Password"

        override fun text(): String = with(params) {
            """
            $webClientUrl/auth/resetPassword?token=$token
            """
        }
    }
}
