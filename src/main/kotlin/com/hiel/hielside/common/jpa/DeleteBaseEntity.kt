package com.hiel.hielside.common.jpa

import com.hiel.hielside.common.utilities.getNowKst
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime

@Audited
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class DeleteBaseEntity : BaseEntity() {
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

    @Column(name = "deleted_at")
    var deletedAt: OffsetDateTime? = null

    @Column(name = "deleted_by")
    var deletedBy: Long? = null

    fun delete(deletedBy: Long) {
        isDeleted = true
        deletedAt = getNowKst()
        this.deletedBy = deletedBy
    }
}
