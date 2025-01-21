CREATE TABLE account_book.asset_categories (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL COMMENT '사용자 아이디',
    name VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '자산 카테고리명',
    budget_price INT UNSIGNED NULL COMMENT '예산',
    is_deleted TINYINT(1) NOT NULL COMMENT '삭제 여부',
    deleted_at DATETIME NULL COMMENT '삭제 일시',
    deleted_by BIGINT UNSIGNED NULL COMMENT '삭제 유저 아이디',
    created_at DATETIME NOT NULL COMMENT '등록 일시',
    created_by BIGINT UNSIGNED NOT NULL COMMENT '등록 사용자 아이디',
    updated_at DATETIME NOT NULL COMMENT '수정 일시',
    updated_by BIGINT UNSIGNED NOT NULL COMMENT '수정 사용자 아이디',
    PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
COMMENT='자산 카테고리'
;


CREATE TABLE account_book.asset_categories_aud (
    rev BIGINT UNSIGNED NOT NULL,
    revtype TINYINT NOT NULL,
    id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL COMMENT '사용자 아이디',
    name VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '자산 카테고리명',
    budget_price INT UNSIGNED NULL COMMENT '예산',
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
COMMENT='자산 카테고리 이력'
;
