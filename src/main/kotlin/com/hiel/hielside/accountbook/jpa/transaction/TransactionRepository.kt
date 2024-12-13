package com.hiel.hielside.accountbook.jpa.transaction

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findAllByTransactionDatetimeBetweenAndUserOrderByTransactionDatetimeDesc(
        transactionDatetimeStart: OffsetDateTime,
        transactionDatetimeEnd: OffsetDateTime,
        user: AccountBookUserEntity,
        pageable: Pageable,
    ): Slice<TransactionEntity>
}
