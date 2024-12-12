package com.hiel.hielside.common.exceptions

import com.hiel.hielside.common.domains.ResultCode

class ServiceException(
    override val resultCode: ResultCode = ResultCode.Common.FAIL,
    override val data: Any? = null,
    vararg args: Any,
) : BaseException(resultCode = resultCode, data = data, args = args)
