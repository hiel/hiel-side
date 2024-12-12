package com.hiel.hielside.common.redis.resetpasswordtoken

import com.hiel.hielside.common.redis.BaseRedisEntity
import com.hiel.hielside.common.utilities.minuteToSecond
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash("SignupToken")
class ResetPasswordTokenRedisEntity(
    @Id
    var userId: Long,
    @Indexed
    var resetPasswordToken: String,
) : BaseRedisEntity {
    @TimeToLive
    override fun getTtlSecond() = 10.minuteToSecond()
}
