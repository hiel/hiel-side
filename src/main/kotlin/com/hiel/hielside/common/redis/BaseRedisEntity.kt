package com.hiel.hielside.common.redis

import org.springframework.data.redis.core.TimeToLive

interface BaseRedisEntity {
    companion object {
        const val UNLIMITED_TTL: Long = -1L
    }

    @TimeToLive
    fun getTtlSecond() = UNLIMITED_TTL
}
