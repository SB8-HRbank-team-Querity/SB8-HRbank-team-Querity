-- 1. 부서 데이터
INSERT INTO department (name, description, established_date, created_at, updated_at)
VALUES ('인사부', '직원 채용 및 관리', '2000-01-01', NOW(), NOW()),
       ('재무부', '회계 및 재무 관리', '2001-02-15', NOW(), NOW()),
       ('영업부', '판매 및 고객 관리', '2002-03-10', NOW(), NOW()),
       ('마케팅부', '광고 및 홍보', '2003-04-05', NOW(), NOW()),
       ('기술부', '시스템 개발 및 유지보수', '2004-05-20', NOW(), NOW()),
       ('연구개발부', '신제품 개발', '2005-06-15', NOW(), NOW()),
       ('고객지원부', '고객 상담 및 지원', '2006-07-10', NOW(), NOW()),
       ('품질관리부', '제품 품질 관리', '2007-08-01', NOW(), NOW()),
       ('물류부', '재고 및 배송 관리', '2008-09-12', NOW(), NOW()),
       ('전략기획부', '회사 전략 및 계획 수립', '2009-10-25', NOW(), NOW());

-- 2. 파일 메타 데이터
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

-- 3. 직원 데이터 (created_at, updated_at 추가됨)
INSERT INTO employee (name, email, employee_number, position, hire_date, status, department_id, profile_image_id, created_at, updated_at)
VALUES ('김민수', 'minsu.kim@company.com', 'EMP-001', 'BACKEND', '2022-03-01', 'ACTIVE', 1, NULL, NOW(), NOW()),
       ('이서연', 'seoyeon.lee@company.com', 'EMP-002', 'FRONTEND', '2021-07-15', 'ACTIVE', 2, NULL, NOW(), NOW()),
       ('박지훈', 'jihoon.park@company.com', 'EMP-003', 'DESIGNER', '2020-11-20', 'ON_LEAVE', 3, NULL, NOW(), NOW()),
       ('최유진', 'yujin.choi@company.com', 'EMP-004', 'HR', '2019-01-10', 'ACTIVE', 1, NULL, NOW(), NOW()),
       ('정현우', 'hyunwoo.jung@company.com', 'EMP-005', 'BACKEND', '2023-02-05', 'ACTIVE', 2, NULL, NOW(), NOW()),
       ('한지민', 'jimin.han@company.com', 'EMP-006', 'FRONTEND', '2022-09-12', 'RESIGNED', 3, NULL, NOW(), NOW()),
       ('오세훈', 'sehun.oh@company.com', 'EMP-007', 'QA', '2021-04-01', 'ACTIVE', 1, NULL, NOW(), NOW()),
       ('윤아린', 'arin.yoon@company.com', 'EMP-008', 'PM', '2018-06-25', 'ACTIVE', 2, NULL, NOW(), NOW()),
       ('임도현', 'dohyun.lim@company.com', 'EMP-009', 'DEVOPS', '2020-08-17', 'ON_LEAVE', 3, NULL, NOW(), NOW()),
       ('장수빈', 'subin.jang@company.com', 'EMP-010', 'BACKEND', '2024-01-02', 'ACTIVE', 1, NULL, NOW(), NOW());

-- 4. 직원 이력 데이터 (JSON 배열 형태, ::jsonb 제거)
INSERT INTO employee_history (type, memo, ip_address, created_at, changed_detail, employee_name, employee_number)
VALUES ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '[
    {
        "propertyName": "dept",
        "before": null,
        "after": "인사팀"
    }
]', '홍길동', 'EMP202001'),
       ('UPDATED', '직급 변경 (사원->대리)', '192.168.0.15', NOW(), '[
           {
               "propertyName": "position",
               "before": "사원",
               "after": "대리"
           }
       ]', '김철수', 'EMP202002'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '[
           {
               "propertyName": "dept",
               "before": null,
               "after": "개발1팀"
           }
       ]', '이영희', 'EMP202003'),
       ('UPDATED', '개인 이메일 수정', '211.234.12.5', NOW(), '[
           {
               "propertyName": "email",
               "before": "old@co.com",
               "after": "changed@co.com"
           }
       ]', '박지민', 'EMP202101'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '[
           {
               "propertyName": "dept",
               "before": null,
               "after": "개발1팀"
           }
       ]', '최준호', 'EMP202102'),
       ('UPDATED', '휴직 처리 (육아휴직)', '10.0.5.21', NOW(), '[
           {
               "propertyName": "status",
               "before": "ACTIVE",
               "after": "ON_LEAVE"
           }
       ]', '윤도현', 'EMP202202'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '[
           {
               "propertyName": "dept",
               "before": null,
               "after": "디자인팀"
           }
       ]', '강미나', 'EMP202201'),
       ('DELETED', '퇴사 처리', '192.168.0.10', NOW(), '[
           {
               "propertyName": "reason",
               "before": null,
               "after": "개인사유"
           }
       ]', '퇴사자A', 'EMP999999'),
       ('UPDATED', '부서 이동', '10.0.5.21', NOW(), '[
           {
               "propertyName": "departmentId",
               "before": "1",
               "after": "6"
           }
       ]', '한가인', 'EMP202302'),
       ('CREATED', '신규 입사자 등록', '127.0.0.1', NOW(), '[
           {
               "propertyName": "dept",
               "before": null,
               "after": "영업팀"
           }
       ]', '송강호', 'EMP202303');

-- 5. 백업 이력 데이터 (INTERVAL 문법 수정: '2' DAY)
INSERT INTO backup_history (worker, started_at, ended_at, status, created_at, file_id)
VALUES ('Admin_Sys', NOW() - INTERVAL '2' DAY, NOW() - INTERVAL '115' MINUTE, 'COMPLETED', NOW(), 6),
       ('Admin_Sys', NOW() - INTERVAL '1' DAY, NOW() - INTERVAL '55' MINUTE, 'COMPLETED', NOW(), 7),
       ('Scheduler', NOW() - INTERVAL '12' HOUR, NULL, 'FAILED', NOW(), NULL),
       ('Admin_Dev', NOW() - INTERVAL '10' HOUR, NOW() - INTERVAL '9' HOUR, 'COMPLETED', NOW(), NULL),
       ('Admin_Sys', NOW() - INTERVAL '8' HOUR, NOW() - INTERVAL '8' HOUR, 'SKIPPED', NOW(), NULL),
       ('Scheduler', NOW() - INTERVAL '6' HOUR, NULL, 'COMPLETED', NOW(), NULL),
       ('Admin_Dev', NOW() - INTERVAL '4' HOUR, NOW() - INTERVAL '235' MINUTE, 'COMPLETED', NOW(), NULL),
       ('Admin_Sys', NOW() - INTERVAL '2' HOUR, NULL, 'COMPLETED', NOW(), NULL),
       ('Scheduler', NOW() - INTERVAL '1' HOUR, NULL, 'COMPLETED', NOW(), NULL),
       ('Scheduler', NOW() - INTERVAL '1' HOUR, NULL, 'IN_PROGRESS', NOW(), NULL),
       ('Admin_Sys', NOW(), NULL, 'COMPLETED', NOW(), NULL);
