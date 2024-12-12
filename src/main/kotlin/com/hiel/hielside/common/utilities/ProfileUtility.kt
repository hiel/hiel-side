package com.hiel.hielside.common.utilities

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class ProfileUtility(
    private val environment: Environment,
) {
    companion object {
        private const val PRODUCTION_PROFILE = "production"
        private const val LOCAL_PROFILE = "local"
    }

    fun isProduction(): Boolean = environment.activeProfiles.any { it == PRODUCTION_PROFILE }

    fun isLocal(): Boolean = environment.activeProfiles.any { it == LOCAL_PROFILE }
}
