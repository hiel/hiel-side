package com.hiel.hielside.common.domains

import org.springframework.data.domain.Slice

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

data class SliceResponseData<E>(
    val content: List<E>,
    val pageSize: Int,
    val hasNext: Boolean,
) {
    companion object {
        fun <D, E> build(slice: Slice<D>, content: List<E>): SliceResponseData<E> {
            return SliceResponseData(
                content = content,
                pageSize = slice.pageable.pageSize,
                hasNext = slice.hasNext(),
            )
        }
    }
}
