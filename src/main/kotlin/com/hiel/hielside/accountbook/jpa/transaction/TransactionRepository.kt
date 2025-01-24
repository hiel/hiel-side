package com.hiel.hielside.accountbook.jpa.transaction

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findFirstByIdAndUser(id: Long, user: AccountBookUserEntity): TransactionEntity?
    fun findFirstByIdAndUserAndIsDeleted(id: Long, user: AccountBookUserEntity, isDeleted: Boolean): TransactionEntity?
    fun findAllByTransactionDatetimeBetweenAndUserAndIsDeleted(
        transactionDatetimeStart: OffsetDateTime,
        transactionDatetimeEnd: OffsetDateTime,
        user: AccountBookUserEntity,
        isDeleted: Boolean,
    ): List<TransactionEntity>
    fun findAllByTransactionDatetimeBetweenAndUserAndIsDeletedOrderByTransactionDatetimeDesc(
        transactionDatetimeStart: OffsetDateTime,
        transactionDatetimeEnd: OffsetDateTime,
        user: AccountBookUserEntity,
        isDeleted: Boolean,
        pageable: Pageable,
    ): Slice<TransactionEntity>
}
