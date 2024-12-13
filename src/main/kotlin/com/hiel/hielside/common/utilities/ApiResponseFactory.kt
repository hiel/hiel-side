package com.hiel.hielside.common.utilities

import com.hiel.hielside.common.domains.ApiResponse
import com.hiel.hielside.common.domains.ResultCode

object ApiResponseFactory {
    fun <D> success(
        data: D? = null,
    ): ApiResponse<D> = ApiResponse(resultCode = ResultCode.Common.SUCCESS, data = data)

    fun <D> failure(
        resultCode: ResultCode,
        message: String? = null,
        data: D? = null,
    ): ApiResponse<D> = ApiResponse(resultCode = resultCode, message = message, data = data)
}
