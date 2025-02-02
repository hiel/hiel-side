package com.hiel.hielside.common.jpa

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Audited
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class ActiveBaseEntity : BaseEntity() {
    @Column(name = "is_active")
    var isActive: Boolean = true
}
