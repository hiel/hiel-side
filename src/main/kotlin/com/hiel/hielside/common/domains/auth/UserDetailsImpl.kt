package com.hiel.hielside.common.domains.auth

import com.hiel.hielside.common.domains.user.UserType
import com.hiel.hielside.common.utilities.EMPTY_STRING
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsImpl(
    val id: Long,
    val email: String,
    val name: String,
    val userType: UserType,
) : UserDetails {
    override fun getAuthorities() = emptyList<GrantedAuthority>()

    override fun getPassword() = EMPTY_STRING

    override fun getUsername() = this.name
}
