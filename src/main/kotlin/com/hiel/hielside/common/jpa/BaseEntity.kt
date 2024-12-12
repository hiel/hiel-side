package com.hiel.hielside.common.jpa

import com.hiel.hielside.common.utilities.getNowKst
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

@Audited
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: OffsetDateTime = getNowKst()

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    var createdBy: Long = 0

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = getNowKst()

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    var updatedBy: Long = 0
}
