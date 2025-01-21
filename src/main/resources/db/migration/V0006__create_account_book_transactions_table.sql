CREATE TABLE account_book.transactions (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL COMMENT '사용자 아이디',
    asset_category_id VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '자산 카테고리 아이디',
    income_expense_type VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '수입/지출 유형',
    transaction_category_id VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '내역 카테고리',
    transaction_datetime DATETIME NOT NULL comment '내역 일시',
    title VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '내역명',
    price INT UNSIGNED NOT NULL COMMENT '금액',
    is_waste INT UNSIGNED NOT NULL COMMENT '낭비 여부',
    is_deleted TINYINT(1) NOT NULL COMMENT '삭제 여부',
    deleted_at DATETIME NULL COMMENT '삭제 일시',
    deleted_by BIGINT UNSIGNED NULL COMMENT '삭제 유저 아이디',
    created_at DATETIME NOT NULL COMMENT '등록 일시',
    created_by BIGINT UNSIGNED NOT NULL COMMENT '등록 사용자 아이디',
    updated_at DATETIME NOT NULL COMMENT '수정 일시',
    updated_by BIGINT UNSIGNED NOT NULL COMMENT '수정 사용자 아이디',
    PRIMARY KEY (id),
    KEY transactions_transaction_datetime_IDX (transaction_datetime) USING BTREE,
    KEY transactions_is_waste_IDX (is_waste) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
COMMENT='내역'
;


CREATE TABLE account_book.transactions_aud (
    rev BIGINT UNSIGNED NOT NULL,
    revtype TINYINT NOT NULL,
    id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL COMMENT '사용자 아이디',
    asset_category_id VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '자산 카테고리 아이디',
    income_expense_type VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '수입/지출 유형',
    transaction_category_id VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '내역 카테고리',
    transaction_datetime DATETIME NOT NULL comment '내역 일시',
    title VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '내역명',
    price INT UNSIGNED NOT NULL COMMENT '금액',
    is_waste INT UNSIGNED NOT NULL COMMENT '낭비 여부',
    is_deleted TINYINT(1) NOT NULL COMMENT '삭제 여부',
    deleted_at DATETIME NULL COMMENT '삭제 일시',
    deleted_by BIGINT UNSIGNED NULL COMMENT '삭제 유저 아이디',
    created_at DATETIME NOT NULL COMMENT '등록 일시',
    created_by BIGINT UNSIGNED NOT NULL COMMENT '등록 사용자 아이디',
    updated_at DATETIME NOT NULL COMMENT '수정 일시',
    updated_by BIGINT UNSIGNED NOT NULL COMMENT '수정 사용자 아이디',
    PRIMARY KEY (rev, id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
COMMENT='내역 이력'
;
