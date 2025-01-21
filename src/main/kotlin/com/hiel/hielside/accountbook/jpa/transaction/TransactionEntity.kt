package com.hiel.hielside.accountbook.jpa.transaction

import com.hiel.hielside.accountbook.domains.IncomeExpenseType
import com.hiel.hielside.accountbook.jpa.assetcategory.AssetCategoryEntity
import com.hiel.hielside.accountbook.jpa.transactioncategory.TransactionCategoryEntity
import com.hiel.hielside.accountbook.jpa.user.AccountBookUserEntity
import com.hiel.hielside.common.jpa.DeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.envers.Audited
import java.time.OffsetDateTime

@Audited
@Entity
@Table(name = "transactions", catalog = "account_book")
class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "income_expense_type", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    var incomeExpenseType: IncomeExpenseType,

    @Column(name = "title")
    var title: String,

    @Column(name = "price")
    var price: Long,

    @Column(name = "is_waste")
    var isWaste: Boolean,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: AccountBookUserEntity,

    @ManyToOne
    @JoinColumn(name = "asset_category_id", nullable = false)
    var assetCategory: AssetCategoryEntity,

    @ManyToOne
    @JoinColumn(name = "transaction_category_id", nullable = false)
    var transactionCategory: TransactionCategoryEntity,

    @Column(name = "transaction_datetime", nullable = false)
    var transactionDatetime: OffsetDateTime
) : DeleteBaseEntity()
