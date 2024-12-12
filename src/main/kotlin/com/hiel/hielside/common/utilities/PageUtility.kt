package com.hiel.hielside.common.utilities

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

private const val DEFAULT_PAGE_SIZE = 30

fun pageOf(page: Int, pageSize: Int = DEFAULT_PAGE_SIZE): Pageable = PageRequest.of(page - 1, pageSize)
