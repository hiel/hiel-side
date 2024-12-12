package com.hiel.hielside.accountbook.jpa.transaction

import com.hiel.hielside.common.jpa.user.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findAllByTransactionDatetimeBetweenAndUserOrderByTransactionDatetimeAsc(
        transactionDatetimeStart: OffsetDateTime,
        transactionDatetimeEnd: OffsetDateTime,
        user: UserEntity,
        pageable: Pageable,
    ): List<TransactionEntity>
}
