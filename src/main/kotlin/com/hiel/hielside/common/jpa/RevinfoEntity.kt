package com.hiel.hielside.common.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import java.time.LocalDateTime

@RevisionEntity
@Table(name = "revinfo", schema = "common")
@Entity
class RevinfoEntity(
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val rev: Long = 0,

    @RevisionTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: LocalDateTime,
)
