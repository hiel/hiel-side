package com.hiel.hielside.common.exceptions

import com.hiel.hielside.common.domains.ResultCode

open class BaseException(
    open val resultCode: ResultCode,
    open val data: Any? = null,
    vararg args: Any,
) : RuntimeException(resultCode.getMessage(*args))
