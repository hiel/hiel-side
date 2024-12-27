package com.hiel.hielside.accountbook.jpa.transactioncategory

import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.common.jpa.DeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.envers.Audited

@Audited
@Entity
@Table(name = "transaction_categories", catalog = "account_book")
class TransactionCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false, length = 20)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: AccountBookUserEntity,
) : DeleteBaseEntity()
