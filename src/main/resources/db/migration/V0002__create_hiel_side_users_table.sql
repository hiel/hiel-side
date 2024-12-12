CREATE TABLE hiel_side.users (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    service_type VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '서비스 유형',
    email VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '이메일',
    password VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '비밀번호',
    name VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '이름',
    user_type VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '사용자 유형',
    user_status VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '사용자 상태',
    created_at DATETIME NOT NULL COMMENT '등록 일시',
    created_by BIGINT UNSIGNED NOT NULL COMMENT '등록 사용자 아이디',
    updated_at DATETIME NOT NULL COMMENT '수정 일시',
    updated_by BIGINT UNSIGNED NOT NULL COMMENT '수정 사용자 아이디',
    PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
COMMENT='사용자'
;


CREATE TABLE hiel_side.users_aud (
    rev BIGINT UNSIGNED NOT NULL,
    revtype TINYINT NOT NULL,
    id BIGINT UNSIGNED NOT NULL,
    service_type VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '서비스 유형',
    email VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '이메일',
    password VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '비밀번호',
    name VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '이름',
    user_type VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '사용자 유형',
    user_status VARCHAR(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '사용자 상태',
    created_at DATETIME NOT NULL COMMENT '등록 일시',
    created_by BIGINT UNSIGNED NOT NULL COMMENT '등록 사용자 아이디',
    updated_at DATETIME NOT NULL COMMENT '수정 일시',
    updated_by BIGINT UNSIGNED NOT NULL COMMENT '수정 사용자 아이디',
    PRIMARY KEY (rev, id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
COMMENT='사용자 이력'
;
