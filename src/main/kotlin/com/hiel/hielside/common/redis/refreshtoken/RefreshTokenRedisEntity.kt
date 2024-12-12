package com.hiel.hielside.common.redis.refreshtoken

import com.hiel.hielside.common.domains.auth.AuthToken
import com.hiel.hielside.common.redis.BaseRedisEntity
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash("RefreshToken")
class RefreshTokenRedisEntity(
    @Id
    var userId: Long,
    var refreshToken: String,
) : BaseRedisEntity {
    @TimeToLive
    override fun getTtlSecond() = AuthToken.REFRESH_TOKEN_EXPIRE_DURATION.seconds
}
