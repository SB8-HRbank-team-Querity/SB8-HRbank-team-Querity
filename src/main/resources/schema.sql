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
    established_date timestamptz  NOT NULL,
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
    hire_date        timestamptz                                        NOT NULL,
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
    id              serial PRIMARY KEY,
    type            varchar(50)  NOT NULL,
    memo            text,
    ip_address      varchar(200) NOT NULL,
    created_at      timestamptz  NOT NULL,
    changed_detail  jsonb        NOT NULL,
    employee_name   varchar(50)  NOT NULL,
    employee_number varchar(50)  NOT NULL,

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

-- 더미데이터
INSERT INTO department (name, description, established_date, created_at, updated_at)
VALUES ('인사부', '직원 채용 및 관리', '2000-01-01 09:00:00+09', NOW(), NOW()),
       ('재무부', '회계 및 재무 관리', '2001-02-15 09:00:00+09', NOW(), NOW()),
       ('영업부', '판매 및 고객 관리', '2002-03-10 09:00:00+09', NOW(), NOW()),
       ('마케팅부', '광고 및 홍보', '2003-04-05 09:00:00+09', NOW(), NOW()),
       ('기술부', '시스템 개발 및 유지보수', '2004-05-20 09:00:00+09', NOW(), NOW()),
       ('연구개발부', '신제품 개발', '2005-06-15 09:00:00+09', NOW(), NOW()),
       ('고객지원부', '고객 상담 및 지원', '2006-07-10 09:00:00+09', NOW(), NOW()),
       ('품질관리부', '제품 품질 관리', '2007-08-01 09:00:00+09', NOW(), NOW()),
       ('물류부', '재고 및 배송 관리', '2008-09-12 09:00:00+09', NOW(), NOW()),
       ('전략기획부', '회사 전략 및 계획 수립', '2009-10-25 09:00:00+09', NOW(), NOW());


INSERT INTO file_meta (origin_name, size, type, path, created_at, updated_at)
VALUES ('profile_01.png', 150200, 'image/png', '/uploads/1.png', NOW(), NOW()),
       ('profile_02.jpg', 88400, 'image/jpeg', '/uploads/2.jpg', NOW(), NOW()),
       ('profile_03.png', 210500, 'image/png', '/uploads/3.png', NOW(), NOW()),
       ('resume_01.pdf', 1048576, 'application/pdf', '/docs/1.pdf', NOW(), NOW()),
       ('logo.svg', 15200, 'image/svg+xml', '/assets/logo.svg', NOW(), NOW()),
       ('backup_v1.zip', 52428800, 'application/zip', '/backups/v1.zip', NOW(), NOW()),
       ('backup_v2.zip', 53428800, 'application/zip', '/backups/v2.zip', NOW(), NOW()),
       ('manual.docx', 45000, 'application/msword', '/docs/manual.docx', NOW(), NOW()),
       ('contract.pdf', 320500, 'application/pdf', '/docs/contract.pdf', NOW(), NOW()),
       ('avatar_tmp.png', 12000, 'image/png', '/tmp/avatar.png', NOW(), NOW());

INSERT INTO employee (name, email, employee_number, position, hire_date, status, department_id, profile_image_id,
                      created_at, updated_at)
VALUES ('홍길동', 'gildong@company.com', 'EMP202001', '팀장', '2020-01-01', 'ACTIVE', 1, 1, NOW(), NOW()),
       ('김철수', 'chulsu@company.com', 'EMP202002', '대리', '2020-02-15', 'ACTIVE', 1, 2, NOW(), NOW()),
       ('이영희', 'young@company.com', 'EMP202003', '과장', '2020-03-10', 'ACTIVE', 2, 3, NOW(), NOW()),
       ('박지민', 'jimin@company.com', 'EMP202101', '사원', '2021-01-20', 'ACTIVE', 2, NULL, NOW(), NOW()),
       ('최준호', 'junho@company.com', 'EMP202102', '차장', '2021-04-05', 'ACTIVE', 2, NULL, NOW(), NOW()),
       ('강미나', 'mina@company.com', 'EMP202201', '대리', '2022-05-12', 'ACTIVE', 3, NULL, NOW(), NOW()),
       ('윤도현', 'dh.yoon@company.com', 'EMP202202', '사원', '2022-06-01', 'ON_LEAVE', 4, 4, NOW(), NOW()),
       ('정수지', 'suzi@company.com', 'EMP202301', '주임', '2023-02-11', 'ACTIVE', 5, NULL, NOW(), NOW()),
       ('한가인', 'gain@company.com', 'EMP202302', '팀장', '2023-03-01', 'ACTIVE', 6, NULL, NOW(), NOW()),
       ('송강호', 'gh.song@company.com', 'EMP202303', '과장', '2023-09-15', 'ACTIVE', 6, NULL, NOW(), NOW());

INSERT INTO employee_history (type, memo, ip_address, created_at, changed_detail, employee_name, employee_number)
VALUES ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '{
    "dept": "인사팀"
}', '홍길동', 'EMP202001'),
       ('UPDATED', '직급 변경 (사원->대리)', '192.168.0.15', NOW(), '{
           "pos_old": "사원",
           "pos_new": "대리"
       }', '김철수', 'EMP202002'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '{
           "dept": "개발1팀"
       }', '이영희', 'EMP202003'),
       ('UPDATED', '개인 이메일 수정', '211.234.12.5', NOW(), '{
           "email": "changed@co.com"
       }', '박지민', 'EMP202101'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '{
           "dept": "개발1팀"
       }', '최준호', 'EMP202102'),
       ('UPDATED', '휴직 처리 (육아휴직)', '10.0.5.21', NOW(), '{
           "status": "ON_LEAVE"
       }', '윤도현', 'EMP202202'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '{
           "dept": "디자인팀"
       }', '강미나', 'EMP202201'),
       ('DELETED', '퇴사 처리', '192.168.0.10', NOW(), '{
           "reason": "개인사유"
       }', '퇴사자A', 'EMP999999'),
       ('UPDATED', '부서 이동', '10.0.5.21', NOW(), '{
           "dept_old": 1,
           "dept_new": 6
       }', '한가인', 'EMP202302'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '{
           "dept": "영업팀"
       }', '송강호', 'EMP202303');

INSERT INTO backup_history (worker, started_at, ended_at, status, created_at, file_id)
VALUES ('Admin_Sys', NOW() - INTERVAL '2 days', NOW() - INTERVAL '115 minutes', 'COMPLETED', NOW(), 6),
       ('Admin_Sys', NOW() - INTERVAL '1 day', NOW() - INTERVAL '55 minutes', 'COMPLETED', NOW(), 7),
       ('Scheduler', NOW() - INTERVAL '12 hours', NULL, 'FAILED', NOW(), NULL),
       ('Admin_Dev', NOW() - INTERVAL '10 hours', NOW() - INTERVAL '9 hours', 'COMPLETED', NOW(), NULL),
       ('Admin_Sys', NOW() - INTERVAL '8 hours', NOW() - INTERVAL '8 hours', 'SKIPPED', NOW(), NULL),
       ('Scheduler', NOW() - INTERVAL '6 hours', NULL, 'COMPLETED', NOW(), NULL),
       ('Admin_Dev', NOW() - INTERVAL '4 hours', NOW() - INTERVAL '235 minutes', 'COMPLETED', NOW(), NULL),
       ('Admin_Sys', NOW() - INTERVAL '2 hours', NULL, 'COMPLETED', NOW(), NULL),
       ('Scheduler', NOW() - INTERVAL '1 hour', NULL, 'COMPLETED', NOW(), NULL),
       ('Admin_Sys', NOW(), NULL, 'COMPLETED', NOW(), NULL);

SELECT *
FROM employee;
SELECT *
FROM department;
SELECT *
FROM employee_history;
SELECT *
FROM file_meta;
SELECT *
FROM backup_history;
