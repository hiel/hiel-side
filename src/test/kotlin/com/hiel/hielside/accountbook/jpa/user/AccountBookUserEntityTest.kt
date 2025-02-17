package com.hiel.hielside.accountbook.jpa.user

import com.hiel.hielside.common.domains.user.UserStatus
import com.hiel.hielside.common.domains.user.UserType
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime
import java.time.ZoneOffset

class AccountBookUserEntityTest : FunSpec({
    context("getTransactionMonthlyRange") {
        test("일자가 내역 시작일보다 같거나 이후일 때의 월 검증 / 날짜 = 20일, 내역 시작일 = 15일") {
            val user = AccountBookUserEntity(
                email = "TEST@TEST.com",
                encryptPassword = "password",
                name = "TEST_USER_NAME",
                userType = UserType.USER,
                userStatus = UserStatus.AVAILABLE,
                transactionStartDay = 15,
            )

            user.getTransactionMonthlyRange(
                OffsetDateTime.of(2024, 2, 20, 0, 0, 0, 0, ZoneOffset.UTC),
            ).let {
                it.first.month.value shouldBe 2
                it.second.month.value shouldBe 3
            }

            user.getTransactionMonthlyRange(
                OffsetDateTime.of(2024, 2, 15, 0, 0, 0, 0, ZoneOffset.UTC),
            ).let {
                it.first.month.value shouldBe 2
                it.second.month.value shouldBe 3
            }
        }

        test("일자가 내역 시작일보다 이전일 때의 월 검증 / 날짜 = 20일, 내역 시작일 = 23일") {
            val user = AccountBookUserEntity(
                email = "TEST@TEST.com",
                encryptPassword = "password",
                name = "TEST_USER_NAME",
                userType = UserType.USER,
                userStatus = UserStatus.AVAILABLE,
                transactionStartDay = 31,
            )

            user.getTransactionMonthlyRange(
                OffsetDateTime.of(2025, 2, 28, 0, 0, 0, 0, ZoneOffset.UTC),
            ).let {
                it.first.monthValue shouldBe 2
                it.second.monthValue shouldBe 3
            }
        }

        test("시작 달의 마지막 일이 내역 시작일보다 크거나 같을 때 / 날짜 = 24년 2월(29일), 내역 시작일 = 15일") {
            val user = AccountBookUserEntity(
                email = "TEST@TEST.com",
                encryptPassword = "password",
                name = "TEST_USER_NAME",
                userType = UserType.USER,
                userStatus = UserStatus.AVAILABLE,
                transactionStartDay = 15,
            )

            user.getTransactionMonthlyRange(
                OffsetDateTime.of(2024, 2, 20, 0, 0, 0, 0, ZoneOffset.UTC),
            ).let {
                it.first.dayOfMonth shouldBe 15
                it.second.dayOfMonth shouldBe 14
            }
        }

        test("시작 달의 마지막 일이 내역 시작일보다 작을 때 / 날짜 = 24년 2월(29일), 내역 시작일 = 30일") {
            val user = AccountBookUserEntity(
                email = "TEST@TEST.com",
                encryptPassword = "password",
                name = "TEST_USER_NAME",
                userType = UserType.USER,
                userStatus = UserStatus.AVAILABLE,
                transactionStartDay = 30,
            )

            user.getTransactionMonthlyRange(
                OffsetDateTime.of(2024, 3, 20, 0, 0, 0, 0, ZoneOffset.UTC),
            ).let {
                it.first.dayOfMonth shouldBe 29
                it.second.dayOfMonth shouldBe 29
            }
        }
    }
})
