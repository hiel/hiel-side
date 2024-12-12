package com.hiel.hielside.common.configurations.jpa

import com.hiel.hielside.common.domains.auth.UserDetailsImpl
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class AuditorAwareImpl : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val principal = SecurityContextHolder.getContext().authentication.principal
        return Optional.ofNullable(if (principal is UserDetailsImpl) principal.id else null)
    }
}
