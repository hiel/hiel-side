package com.hiel.hielside.common.jpa

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.OffsetDateTime

@MappedSuperclass
abstract class DeleteBaseEntity : BaseEntity() {
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

    @Column(name = "deleted_at")
    var deletedAt: OffsetDateTime? = null

    @Column(name = "deleted_by")
    var deletedBy: Long? = null
}
