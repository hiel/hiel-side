package com.hiel.hielside.common.domains

interface ResultCode {
    fun getMessage(vararg args: Any): String?

    enum class Common(private val message: String? = null) : ResultCode {
        SUCCESS,
        FAIL("요청에 실패하였습니다."),
        ;

        override fun getMessage(vararg args: Any) = message?.let { String.format(it, *args) }
    }

    enum class File(private val message: String? = null) : ResultCode {
        FAILED_TO_UPLOAD_FILE("파일 업로드에 실패했습니다."),
        FILE_SIZE_TOO_LARGE("용량이 너무 큽니다."),
        INVALID_FILE_EXTENSION("파일은 %s 확장자만 업로드 가능합니다."),
        ;

        override fun getMessage(vararg args: Any) = message?.let { String.format(it, *args) }
    }

    enum class Auth(private val message: String?) : ResultCode {
        INVALID_FORMAT_EMAIL("잘못된 형식의 이메일입니다."),
        LENGTH_TOO_SHORT_PASSWORD("비밀번호는 최소 %d자 이상이어야 합니다."),
        LENGTH_TOO_SHORT_NAME("이름은 최소 %d자 이상이어야 합니다."),
        AUTHENTICATION_FAIL("인증에 실패하였습니다."),
        INVALID_TOKEN("잘못된 인증 정보입니다."),
        EXPIRED_TOKEN("만료된 인증 정보입니다."),
        NOT_EXIST_USER("없는 회원입니다"),
        EXPIRED_ACCESS_TOKEN("만료된 인증 정보입니다."),
        EXPIRED_REFRESH_TOKEN("만료된 인증 정보입니다."),
        DUPLICATED_EMAIL("중복된 이메일이 있습니다."),
        INVALID_EMAIL_OR_PASSWORD("아이디 또는 비밀번호가 일치하지 않습니다."),
        INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
        ;

        override fun getMessage(vararg args: Any) = message?.let { String.format(it, *args) }
    }
}
