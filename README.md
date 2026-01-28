## 쿼리티있조
[쿼리티있조 노션 링크](https://www.notion.so/ohgiraffers/2-Query-2ee649136c118044b776e0173c23f1f9?source=copy_link)
***
## 팀원 구성
- 이재준 (jaejoon0520@naver.com)
- 홍성휘 (gkemg2017@gmail.com)
- 이예은 (dldpdms0000@naver.com)
- 황진서 (workspace8612@gmail.com)
- 박하민 (parkhamint@naver.com)
***
## 프로젝트 소개
- <img width="1905" height="909" alt="image" src="https://github.com/user-attachments/assets/e0e26d5e-f3c4-46c4-a4bd-d949c96c21dd" />
- **HR Bank**
- Batch로 데이터를 관리하는 Open EMS
- 기업의 인적 자원을 안전하게 관리하는 서비스
- 프로젝트 기간: 2026.01.20 ~ 2026.01.29
***
## 기술 스택
- Spring Boot
- Spring Data JPA
- PostgreSQL
- springdoc-openapi
- MapStruct
- Railway.io
- queryDSL, Specification
- Discord Webhook
- H2 Database
- Apache POI
- 공통 Tool: Git & Github, Discord, Notion
***
## 팀원별 구현 기능 상세
### 박하민
- <img width="1919" height="905" alt="image" src="https://github.com/user-attachments/assets/e340326b-c334-4c60-a01e-60fa02fd954d" />
- **부서 등록**
  - {이름}, {설명}, {설립일}을 입력해 부서를 등록할 수 있습니다.
      - {이름}은 중복될 수 없습니다.
- **부서 수정**
  - {이름}, {설명}, {설립일}을 수정할 수 있습니다.
      - {이름}은 중복될 수 없습니다.
- **부서 삭제**
  - 소속된 직원이 없는 경우에만 부서를 삭제할 수 있습니다.
- **부서 목록 조회**
  - {이름 또는 설명}으로 부서 목록을 조회할 수 있습니다.
      - {이름 또는 설명}는 부분 일치 조건입니다.
      - 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.
  - {이름}, {설립일}로 정렬 및 페이지네이션을 구현합니다.
      - 여러 개의 정렬 조건 중 선택적으로 1개의 정렬 조건만 가질 수 있습니다.
      - 정확한 페이지네이션을 위해 {이전 페이지의 마지막 요소 ID}를 활용합니다.
      - 화면을 고려해 적절한 페이지네이션 전략을 선택합니다.
- **쿼리 고도화**
  - queryDSL을 활용하여 복잡한 쿼리의 가독성을 높일 수 있습니다.
- **부서 일괄 생성**
  - 엑셀 파일을 등록하여 여러개의 부서를 일괄 생성합니다.
    - 중복된 부서명이거나 {이름}, {설명}, {설립일} 중 하나라도 없는 부서 정보라면 부서를 생성하지 않습니다.
- **부서 일괄 추출**
  - 생성된 부서 목록을 엑셀 파일로 추출합니다.
### 이예은
- <img width="1919" height="909" alt="image" src="https://github.com/user-attachments/assets/e7def04e-c870-4548-ad55-05ff28140332" />
- **직원 등록**
  - {이름}, {이메일}, {부서}, {직함}, {입사일}, {프로필 이미지}를 통해 직원을 등록할 수 있습니다.
      - {이메일}은 다른 직원과 중복되면 안됩니다.
      - {프로필 이미지}는 선택적으로 등록할 수 있습니다.
      - {프로필 이미지}는 이어지는 파일 관리 요구사항에 따라 등록합니다.
      - {사원 번호}는 자동으로 부여되어야 합니다. 규칙은 자유롭게 정의하세요.
      - {상태}는 자동으로 `재직중`상태로 초기화합니다.
- **직원 정보 수정**
  - {사원 번호}를 제외한 다른 속성은 모두 수정할 수 있습니다.
  - {이메일}은 다른 직원과 중복되면 안됩니다.
- **직원 정보 삭제**
  - 직원을 삭제하면 프로필 이미지도 같이 삭제되어야합니다.
  - `퇴사`는 삭제가 아닌 수정으로 처리해야합니다.
- **직원 정보 목록 조회**
  - {이름 또는 이메일}, {부서}, {직함}, {사원번호}, {입사일}, {상태}로 직원 목록을 조회할 수 있습니다.
      - {이름 또는 이메일}, {부서}, {직함}, {사원번호}는 부분 일치 조건입니다.
      - {입사일}은 범위 조건입니다.
      - {상태}는 완전 일치 조건입니다.
      - 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.
  - {이름}, {입사일}, {사원번호}로 정렬 및 페이지네이션을 구현합니다.
      - 여러 개의 정렬 조건 중 선택적으로 1개의 정렬 조건만 가질 수 있습니다.
      - 정확한 페이지네이션을 위해 {이전 페이지의 마지막 요소 ID}를 활용합니다.
      - 화면을 고려해 적절한 페이지네이션 전략을 선택합니다.
- **직원 정보 상세 조회**
  - {id}로 직원의 상세 정보를 조회할 수 있습니다.
- **대시보드 관리**
  - 직원 수를 조회할 수 있습니다.
  - 직원 수 추이를 조회할 수 있습니다.
  - 직원의 부서 분포를 조회할 수 있습니다.
- **쿼리 고도화**
  - specification을 활용하여 복잡한 쿼리의 가독성을 높일 수 있습니다.
### 홍성휘
- <img width="571" height="366" alt="image" src="https://github.com/user-attachments/assets/5c17ecd6-175f-454b-bff1-20502a0a70d2" />
- **파일 저장**
  - {메타 정보}는 데이터베이스에, {실제 파일}은 로컬 디스크에 저장합니다.
- **파일 다운로드**
  - {메타 정보}의 {id}를 통해 파일을 다운로드 할 수 있습니다.
- **통합 에러 처리**
  - 프로젝트의 클라이언트에 일관된 에러 응답 제공, 서버 로그 관리를 편하게 하기 위해서 예외 처리 시스템 도입합니다.
    - 모든 비즈니스 로직상의 예외는 `BussinessException` 을 통해서 처리하고, 응답은 통일된 `JSON` 포맷으로 내려갑니다.
- **에러 코드와 디스코드 연동**
  - 웹훅을 이용하여 에러가 발생할 시 디스코드 채널로 에러 명세 메시지를 보낼 수 있습니다.
- **H2 데이터베이스 활용**
  - 로컬 개발환경에서 H2 데이터베이스를 활용해 간단하게 DB를 구성해보세요.
### 황진서
- <img width="1919" height="910" alt="image" src="https://github.com/user-attachments/assets/b7ead9b9-94e8-461d-b629-f12e8df188e6" />
- **이력 등록**
  - 직원 추가, 직원 정보 수정 화면에서 입력하는 메모 정보를 통해 등록합니다.
      - {유형}, {대상 직원 사번}, {변경 상세 내용}은 로직 흐름에 따라 적절히 판단합니다.
      - {메모}는 화면에서 입력한 메모 정보이며, 선택적인 값입니다.
      - {IP 주소}는 서버에서 자동으로 추출 후 저장합니다.
- **이력 목록 조회**
  - {대상 직원 사번}, {메모}, {IP 주소}, {시간}, {유형}으로 이력 목록을 조회할 수 있습니다.
      - {대상 직원 사번}, {메모}, {IP 주소}은 부분 일치 조건입니다.
      - {시간}은 범위 조건입니다.
      - {유형}은 완전 일치 조건입니다.
      - 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.
  - {IP 주소}, {시간}으로 정렬 및 페이지네이션을 구현합니다.
      - 여러 개의 정렬 조건 중 선택적으로 1개의 정렬 조건만 가질 수 있습니다.
      - 정확한 페이지네이션을 위해 {이전 페이지의 마지막 요소 ID}를 활용합니다.
      - 화면을 고려해 적절한 페이지네이션 전략을 선택합니다.
  - 데이터 크기를 고려, {변경 상세 내용}은 포함하지 않습니다.
- **이력 상세 변경 내용 조회**
  - {id}로 이력의 {변경 상세 내용}을 조회합니다.
- **쿼리 고도화**
  - specification을 활용하여 복잡한 쿼리의 가독성을 높일 수 있습니다.
### 이재준
- <img width="1919" height="908" alt="image" src="https://github.com/user-attachments/assets/79bdb6de-6beb-40f7-8a4e-b79e7b24d2f2" />
- **데이터 백업**
- **STEP.1** 데이터 백업 필요 여부를 판단합니다.
    - 가장 최근 완료된 배치 작업 시간 이후 직원 데이터가 변경된 경우에 데이터 백업이 필요한 것으로 간주합니다.
    - 백업이 필요 없다면 건너뜀 상태로 배치 이력을 저장하고 프로세스를 종료합니다.
- **STEP.2** 데이터 백업 필요 시 데이터 백업 이력을 등록합니다.
    - {작업자}는 요청자의 `IP 주소`입니다.
    - {상태}는 `진행중`으로 설정합니다.
- **STEP.3** 실제 데이터 백업 작업을 수행합니다.
    - 이 단계에서 API를 통해 **STEP.2**에서 등록한 데이터를 조회할 수 있어야 합니다.
    - 전체 직원 정보를 파일 관리 요구사항에 따라 CSV 파일로 저장합니다. 본 프로젝트에서는 압축효율은 고려하지 않습니다.
        - 모든 직원을 한번에 처리하는 경우 OOM 이슈가 발생할 수 있으니 적절한 방안을 강구해보세요.
- **STEP.4-1** 백업이 성공하면 **STEP.2**에서 등록한 데이터 백업 이력을 다음과 같이 수정합니다.
    - **{상태}**: `완료`
    - **{종료 시간}**: 현재 시간
    - **{백업 파일}:** 백업 파일 정보
- **STEP.4-2** 백업이 실패하면 **STEP.2**에서 등록한 데이터 백업 이력을 다음과 같이 수정합니다.
    - **STEP.3**에서 저장하던 파일을 삭제합니다.
    - 에러 로그를 **파일 관리 요구사항**에 따라 `.log` 파일로 저장합니다.
    - {상태}: `실패`
    - {종료 시간}: 현재 시간
    - {백업 파일}: 에러 로그 파일 정보
- **배치에 의한 데이터 백업**
- 데이터 백업 프로세스를 일정한 주기(1시간)마다 자동으로 반복합니다.
    - 배치 주기는 애플리케이션 설정을 통해 주입할 수 있어야 합니다.
    - `Spring Scheduler`를 활용해 구현하세요.
- {작업자}는 `system`으로 입력합니다.
- **데이터 백업 이력 목록 조회**
- {작업자}, {시작 시간}, {상태}로 이력 목록을 조회할 수 있습니다.
    - {작업자}는 부분 일치 조건입니다.
    - {시작 시간}는 범위 조건입니다.
    - {상태}는 완전 일치 조건입니다.
    - 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.
- {시작 시간}, {종료 시간}으로 정렬 및 페이지네이션을 구현합니다.
    - 여러 개의 정렬 조건 중 선택적으로 1개의 정렬 조건만 가질 수 있습니다.
    - 정확한 페이지네이션을 위해 {이전 페이지의 마지막 요소 ID}를 활용합니다.
    - 화면을 고려해 적절한 페이지네이션 전략을 선택합니다.
- **쿼리 고도화**
- queryDSL을 활용하여 복잡한 쿼리의 가독성을 높일 수 있습니다.
- **좀비 데이터 모니터링**
  - 고착화를 방지하기 위해 30분이 지나도 진행 중인 데이터를 실패 처리할 수 있습니다.
- **배치 시스템과 Gmail 연동**
  - 백업 여부에 따라 Gmail과 연동하여 백업 메시지를 보낼 수 있습니다.
***
## 파일 구조
```
src
├─main
│  ├─generated
│  │  └─com
│  │      └─sprint
│  │          └─mission
│  │              └─sb8hrbankteamquerity
│  │                  ├─entity
│  │                  │  │  QBackupHistory.java
│  │                  │  │  QDepartment.java
│  │                  │  │  QEmployee.java
│  │                  │  │  QEmployeeHistory.java
│  │                  │  │  QFileMeta.java
│  │                  │  └─base
│  │                  │          QBaseEntity.java
│  │                  │          QBaseUpdatableEntity.java         
│  │                  └─mapper
│  │                          BackupHistoryMapperImpl.java
│  │                          DepartmentMapperImpl.java
│  │                          EmployeeHistoryMapperImpl.java
│  │                          EmployeeMapperImpl.java
│  │                          FileMetaMapperImpl.java           
│  ├─java
│  │  └─com
│  │      └─sprint
│  │          └─mission
│  │              └─sb8hrbankteamquerity
│  │                  │  Sb8HrbankTeamQuerityApplication.java 
│  │                  ├─common
│  │                  │  └─util
│  │                  │          ExcelUtils.java
│  │                  │          IpUtil.java        
│  │                  ├─component
│  │                  │      BackupScheduler.java    
│  │                  ├─config
│  │                  │      QuerydslConfig.java     
│  │                  ├─controller
│  │                  │  │  BackupHistoryController.java
│  │                  │  │  DepartmentController.java
│  │                  │  │  EmployeeController.java
│  │                  │  │  EmployeeHistoryController.java
│  │                  │  │  FileController.java
│  │                  │  └─docs
│  │                  │          BackupHistoryApi.java
│  │                  │          DepartmentApi.java
│  │                  │          EmployeeApi.java
│  │                  │          EmployeeHistoryApi.java
│  │                  │          FileMetaApi.java      
│  │                  ├─dto
│  │                  │  ├─BackupHistory
│  │                  │  │      BackupHistoryDto.java
│  │                  │  │      BackupHistoryPageRequest.java
│  │                  │  │      BackupHistorySearchCondition.java
│  │                  │  │      CursorPageResponseBackupHistoryDto.java     
│  │                  │  ├─dashBoard
│  │                  │  │      EmployeeCountRequest.java
│  │                  │  │      EmployeeDistributionDto.java
│  │                  │  │      EmployeeDistributionRequest.java
│  │                  │  │      EmployeeTrendDto.java
│  │                  │  │      EmployeeTrendRequest.java     
│  │                  │  ├─department
│  │                  │  │      CursorPageResponseDepartmentDto.java
│  │                  │  │      DepartmentCreateRequest.java
│  │                  │  │      DepartmentDto.java
│  │                  │  │      DepartmentPageRequest.java
│  │                  │  │      DepartmentUpdateRequest.java     
│  │                  │  ├─discord
│  │                  │  │      DiscordEmbedRequest.java
│  │                  │  │      DiscordFieldRequest.java
│  │                  │  │      DiscordMessageRequest.java   
│  │                  │  ├─employee
│  │                  │  │      EmployeeCreateRequest.java
│  │                  │  │      EmployeeDto.java
│  │                  │  │      EmployeePageResponse.java
│  │                  │  │      EmployeeSearchDto.java
│  │                  │  │      EmployeeUpdateRequest.java     
│  │                  │  ├─EmployeeHistory
│  │                  │  │      ChangeLogDetailDto.java
│  │                  │  │      ChangeLogDto.java
│  │                  │  │      CursorPageResponseChangeLogDto.java
│  │                  │  │      DiffDto.java
│  │                  │  │      EmployeeHistoryFilter.java
│  │                  │  │      EmployeeHistorySaveRequest.java    
│  │                  │  ├─error
│  │                  │  │      ErrorResponse.java    
│  │                  │  └─fileMeta
│  │                  │          FileMetaResponse.java         
│  │                  ├─entity
│  │                  │  │  BackupHistory.java
│  │                  │  │  Department.java
│  │                  │  │  Employee.java
│  │                  │  │  EmployeeHistory.java
│  │                  │  │  FileMeta.java
│  │                  │  ├─base
│  │                  │  │      BaseEntity.java
│  │                  │  │      BaseUpdatableEntity.java  
│  │                  │  └─enums
│  │                  │          BackupHistoryStatus.java
│  │                  │          EmployeeHistoryType.java
│  │                  │          EmployeeStatus.java
│  │                  │          sortType.java          
│  │                  ├─exception
│  │                  │      BackupHistoryErrorCode.java
│  │                  │      BusinessException.java
│  │                  │      DepartmentErrorCode.java
│  │                  │      EmployeeErrorCode.java
│  │                  │      EmployeeHistoryErrorCode.java
│  │                  │      ErrorCode.java
│  │                  │      FileErrorCode.java
│  │                  │      GlobalErrorCode.java
│  │                  │      GlobalExceptionHandler.java    
│  │                  ├─mapper
│  │                  │      BackupHistoryMapper.java
│  │                  │      DepartmentMapper.java
│  │                  │      EmployeeHistoryMapper.java
│  │                  │      EmployeeMapper.java
│  │                  │      FileMetaMapper.java  
│  │                  ├─repository
│  │                  │  │  BackupHistoryRepository.java
│  │                  │  │  BackupHistoryRepositoryCustom.java
│  │                  │  │  DepartmentRepository.java
│  │                  │  │  DepartmentRepositoryCustom.java
│  │                  │  │  EmployeeHistoryRepository.java
│  │                  │  │  EmployeeRepository.java
│  │                  │  │  FileRepository.java 
│  │                  │  └─impl
│  │                  │          BackupHistoryRepositoryImpl.java
│  │                  │          DepartmentRepositoryImpl.java         
│  │                  └─service
│  │                      │  BackupHistoryService.java
│  │                      │  DepartmentService.java
│  │                      │  DiscordWebhookService.java
│  │                      │  EmailService.java
│  │                      │  EmployeeHistoryService.java
│  │                      │  EmployeeService.java
│  │                      │  FileStorageService.java
│  │                      ├─impl
│  │                      │      BackupHistoryServiceImpl.java
│  │                      │      DepartmentServiceImpl.java
│  │                      │      DiscordWebhookServiceImpl.java
│  │                      │      EmailServiceImpl.java
│  │                      │      EmployeeHistoryServiceImpl.java
│  │                      │      EmployeeServiceImpl.java
│  │                      │      FileStorageServiceImpl.java    
│  │                      └─specification
│  │                              EmployeeHistorySpecification.java
│  │                              EmployeeSpecification.java           
│  └─resources
│      │  application-local.yaml
│      │  application-prod.yaml
│      │  application.yaml
│      │  data-h2.sql
│      │  data-pg.sql
│      │  schema.sql
│      └─static
│          │  index.html
│          │  logo.ico
│          └─assets
│                  index-53YfCFDQ.css
│                  index-BL7rXp4c.js
│                  logo-D8Y9H90G.ico
│                  Pretendard-Black-B7X87vPW.woff2
│                  Pretendard-Black-CGKHU3YP.woff
│                  Pretendard-Bold-BYNivUXw.woff2
│                  Pretendard-Bold-DD7wHHNl.woff
│                  Pretendard-ExtraBold-C0vVUedy.woff2
│                  Pretendard-ExtraBold-DkRXFB8B.woff
│                  Pretendard-ExtraLight-Bi0YRlFr.woff2
│                  Pretendard-ExtraLight-CmnYHmfp.woff
│                  Pretendard-Light-BSr3DBFh.woff
│                  Pretendard-Light-knQmDAda.woff2
│                  Pretendard-Medium-Cs2k_Pp2.woff
│                  Pretendard-Medium-Dw2vNklR.woff2
│                  Pretendard-Regular-BhrLQoBv.woff2
│                  Pretendard-Regular-D5CgADJ9.woff
│                  Pretendard-SemiBold-ClEDdoZU.woff2
│                  Pretendard-SemiBold-SXfe8JY8.woff
│                  Pretendard-Thin-Cq3km6ap.woff
│                  Pretendard-Thin-DWJVAZ2K.woff2            
└─test
    └─java
        └─com
            └─sprint
                └─mission
                    └─sb8hrbankteamquerity
                            Sb8HrbankTeamQuerityApplicationTests.java
```

## 구현 홈페이지
http://sb8-hrbank-team-querity-production.up.railway.app

## 프로젝트 회고록
(제작한 발표자료 링크 혹은 첨부파일 첨부)
