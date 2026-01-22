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
    description      text,
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
    status           varchar(50)                                        NOT NULL DEFAULT '재직중',
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    department_id    int                                                NOT NULL,
    profile_image_id int,

    CONSTRAINT check_status CHECK (status IN ('재직중', '휴직중', '퇴사')),
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

    CHECK (type IN ('직원 추가', '정보 수정', '직원 삭제'))
);

-- 5. 백업 데이터 테이블
CREATE TABLE backup_history
(
    id         serial PRIMARY KEY,
    worker     varchar(50)               NOT NULL,
    started_at timestamptz               NOT NULL,
    ended_at   timestamptz,
    status     varchar(50) DEFAULT '진행중' NOT NULL,
    created_at timestamptz               NOT NULL,
    file_id    int REFERENCES file_meta (id),

    CHECK (status IN ('진행중', '완료', '실패', '건너뜀'))
);

-- 인덱스
CREATE INDEX idx_file_meta_created_at ON file_meta (created_at);

-- 더미데이터
INSERT INTO department (name, description, established_date, employee_count, created_at, updated_at)
VALUES ('개발팀', '백엔드/프론트엔드 개발', '2019-01-01', 3, NOW(), NOW()),
       ('인사팀', '채용 및 인사관리', '2019-03-01', 2, NOW(), NOW()),
       ('기획팀', '서비스 기획', '2020-01-01', 1, NOW(), NOW()),
       ('디자인팀', 'UI/UX 디자인', '2020-05-01', 1, NOW(), NOW()),
       ('마케팅팀', '마케팅 전략', '2021-01-01', 1, NOW(), NOW()),
       ('영업팀', '영업 관리', '2021-06-01', 1, NOW(), NOW()),
       ('운영팀', '시스템 운영', '2022-01-01', 0, NOW(), NOW()),
       ('재무팀', '회계 및 재무', '2018-01-01', 0, NOW(), NOW()),
       ('CS팀', '고객 지원', '2022-05-01', 0, NOW(), NOW()),
       ('QA팀', '품질 관리', '2023-01-01', 0, NOW(), NOW());
INSERT INTO file_meta (origin_name, size, type, path)
VALUES ('profile1.png', 100000, 'image/png', '/uploads/profile1.png'),
       ('profile2.png', 110000, 'image/png', '/uploads/profile2.png'),
       ('profile3.png', 120000, 'image/png', '/uploads/profile3.png'),
       ('profile4.png', 130000, 'image/png', '/uploads/profile4.png'),
       ('profile5.png', 140000, 'image/png', '/uploads/profile5.png'),
       ('profile6.png', 150000, 'image/png', '/uploads/profile6.png'),
       ('profile7.png', 160000, 'image/png', '/uploads/profile7.png'),
       ('profile8.png', 170000, 'image/png', '/uploads/profile8.png'),
       ('profile9.png', 180000, 'image/png', '/uploads/profile9.png'),
       ('profile10.png', 190000, 'image/png', '/uploads/profile10.png');
INSERT INTO employee (name, email, employee_number, position, hire_date, status, department_id, profile_image_id)
VALUES ('김예은', 'user1@test.com', 'EMP001', '백엔드 개발자', '2022-01-10', '재직중', 1, 1),
       ('이민수', 'user2@test.com', 'EMP002', '프론트엔드 개발자', '2022-03-15', '재직중', 1, 2),
       ('박지현', 'user3@test.com', 'EMP003', '인사 담당자', '2021-07-01', '재직중', 2, 3),
       ('최영호', 'user4@test.com', 'EMP004', '기획자', '2023-01-01', '재직중', 3, 4),
       ('정수빈', 'user5@test.com', 'EMP005', '디자이너', '2020-11-01', '휴직중', 4, 5),
       ('한지민', 'user6@test.com', 'EMP006', '마케터', '2021-09-01', '재직중', 5, 6),
       ('오세훈', 'user7@test.com', 'EMP007', '영업 사원', '2022-04-01', '재직중', 6, 7),
       ('윤아름', 'user8@test.com', 'EMP008', '운영 담당', '2023-02-01', '재직중', 7, 8),
       ('강동원', 'user9@test.com', 'EMP009', '재무 담당', '2020-06-01', '퇴사', 8, 9),
       ('서지수', 'user10@test.com', 'EMP010', 'QA 엔지니어', '2024-01-01', DEFAULT, 10, 10);
INSERT INTO employee_history (type, memo, ip_address, created_at, changed_detail, employee_name, employee_number)
VALUES ('직원 추가', '신규 입사', '10.0.0.1', NOW(), '{
    "action": "create"
}', '김예은', 'EMP001'),
       ('정보 수정', '직급 변경', '10.0.0.2', NOW(), '{
           "position": "선임 개발자"
       }', '이민수', 'EMP002'),
       ('직원 추가', '신규 입사', '10.0.0.3', NOW(), '{
           "action": "create"
       }', '박지현', 'EMP003'),
       ('정보 수정', '부서 이동', '10.0.0.4', NOW(), '{
           "department": "기획팀"
       }', '최영호', 'EMP004'),
       ('정보 수정', '휴직 처리', '10.0.0.5', NOW(), '{
           "status": "휴직중"
       }', '정수빈', 'EMP005'),
       ('직원 추가', '신규 입사', '10.0.0.6', NOW(), '{
           "action": "create"
       }', '한지민', 'EMP006'),
       ('정보 수정', '이메일 변경', '10.0.0.7', NOW(), '{
           "email": "new7@test.com"
       }', '오세훈', 'EMP007'),
       ('직원 추가', '신규 입사', '10.0.0.8', NOW(), '{
           "action": "create"
       }', '윤아름', 'EMP008'),
       ('직원 삭제', '퇴사 처리', '10.0.0.9', NOW(), '{
           "status": "퇴사"
       }', '강동원', 'EMP009'),
       ('정보 수정', '프로필 변경', '10.0.0.10', NOW(), '{
           "profile_image": "updated"
       }', '서지수', 'EMP010');
INSERT INTO backup_history (worker, started_at, ended_at, status, created_at, file_id)
VALUES ('admin', NOW() - INTERVAL '30 min', NOW(), '완료', NOW(), 1),
       ('admin', NOW() - INTERVAL '40 min', NOW(), '완료', NOW(), 2),
       ('system', NOW() - INTERVAL '20 min', NOW(), '완료', NOW(), 3),
       ('system', NOW() - INTERVAL '15 min', NOW(), '완료', NOW(), 4),
       ('admin', NOW() - INTERVAL '10 min', NOW(), '완료', NOW(), 5),
       ('admin', NOW() - INTERVAL '5 min', NOW(), '완료', NOW(), 6),
       ('system', NOW() - INTERVAL '1 hour', NOW(), '실패', NOW(), 7),
       ('system', NOW() - INTERVAL '2 hour', NOW(), '완료', NOW(), 8),
       ('admin', NOW() - INTERVAL '3 hour', NOW(), '완료', NOW(), 9),
       ('admin', NOW() - INTERVAL '4 hour', NOW(), '완료', NOW(), 10);


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
