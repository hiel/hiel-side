package com.hiel.hielside.common.domains

abstract class BaseApiResponse<T>(
    open val resultCode: ResultCode,
    open val message: String? = null,
    open val data: T? = null,
)

data class ApiResponse<D>(
    override val resultCode: ResultCode,
    override val message: String? = null,
    override val data: D? = null,
) : BaseApiResponse<D>(resultCode, message, data)

data class ApiSliceResponse<E>(
    override val data: SliceData<E>? = null
) : BaseApiResponse<SliceData<E>>(ResultCode.Common.SUCCESS, null, data)

data class SliceData<E>(
    val pageSize: Int? = null,
    val list: List<E>,
)
