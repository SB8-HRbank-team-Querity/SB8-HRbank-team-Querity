DROP TABLE IF EXISTS backup_history CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS file_meta CASCADE;
DROP TABLE IF EXISTS employee_history CASCADE;
DROP TABLE IF EXISTS department CASCADE;

-- 1. 부서 테이블
CREATE TABLE department
(
    id               serial PRIMARY KEY,
    name             varchar(100) NOT NULL UNIQUE,
    description      text         NOT NULL,
    established_date date         NOT NULL,
    created_at       timestamptz  NOT NULL,
    updated_at       timestamptz  NOT NULL
);

-- 2. 파일 메타 데이터 테이블
CREATE TABLE file_meta
(
    id          SERIAL PRIMARY KEY,
    origin_name VARCHAR(50)                                        NOT NULL,
    size        BIGINT                                             NOT NULL,
    type        VARCHAR(100)                                       NOT NULL,
    path        VARCHAR(500)                                       NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 3. 직원 테이블
CREATE TABLE IF NOT EXISTS employee
(
    id               SERIAL PRIMARY KEY,
    name             varchar(50)                                        NOT NULL,
    email            varchar(100)                                       NOT NULL,
    employee_number  varchar(50)                                        NOT NULL,
    position         varchar(50)                                        NOT NULL,
    hire_date        date                                               NOT NULL,
    status           varchar(50)                                        NOT NULL DEFAULT 'ACTIVE',
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    department_id    int                                                NOT NULL,
    profile_image_id int,

    CONSTRAINT check_status CHECK (status IN ('ACTIVE', 'ON_LEAVE', 'RESIGNED')),
    CONSTRAINT fk_department_id FOREIGN KEY (department_id) REFERENCES department (id),
    CONSTRAINT fk_profile_image_id FOREIGN KEY (profile_image_id) REFERENCES file_meta (id),
    CONSTRAINT uk_email UNIQUE (email),
    CONSTRAINT uk_employee_number UNIQUE (employee_number)
);

-- 4. 직원 정보 수정 이력 테이블
CREATE TABLE employee_history
(
    id               serial PRIMARY KEY,
    type             varchar(50)  NOT NULL,
    memo             text,
    ip_address       varchar(200) NOT NULL,
    created_at       timestamptz  NOT NULL,
    changed_detail   jsonb        NOT NULL,
    employee_name    varchar(50)  NOT NULL,
    employee_number  varchar(50)  NOT NULL,
    profile_image_id int,

    CHECK (type IN ('CREATED', 'UPDATED', 'DELETED'))
);

-- 5. 백업 데이터 테이블
CREATE TABLE backup_history
(
    id         serial PRIMARY KEY,
    worker     varchar(50)                   NOT NULL,
    started_at timestamptz                   NOT NULL,
    ended_at   timestamptz,
    status     varchar(50) DEFAULT 'SKIPPED' NOT NULL,
    created_at timestamptz                   NOT NULL,
    file_id    int REFERENCES file_meta (id),

    CHECK (status IN ('SKIPPED', 'COMPLETED', 'FAILED', 'IN_PROGRESS'))
);
-- 인덱스
CREATE INDEX idx_file_meta_created_at ON file_meta (created_at);
