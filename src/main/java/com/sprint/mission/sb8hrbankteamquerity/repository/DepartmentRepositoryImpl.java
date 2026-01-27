package com.sprint.mission.sb8hrbankteamquerity.repository;

import static com.sprint.mission.sb8hrbankteamquerity.entity.QEmployee.employee;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import com.sprint.mission.sb8hrbankteamquerity.entity.QDepartment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom {

    // 쿼리를 날리는 핵심 도구 (configuration으로 bean 설정 완료)
    private final JPAQueryFactory queryFactory;
    // 쿼리 작성을 위한 Qclass 객체
    private final QDepartment department = QDepartment.department;

    @Override
    public List<Department> findAllByCursor(DepartmentPageRequest departmentPageRequest, Pageable pageable) {
        // 1. 기본값 및 타입 변환 처리
        boolean isAsc = !"desc".equalsIgnoreCase(departmentPageRequest.sortDirection());
        String sortField = departmentPageRequest.sortField();

        // 커서(String)를 날짜로 변환 (기본 정렬이 설립일이므로 필요)
        LocalDate lastDateValue = null;
        if ("establishedDate".equals(sortField) || !StringUtils.hasText(sortField)) {
            if (StringUtils.hasText(departmentPageRequest.cursor())) {
                lastDateValue = LocalDate.parse(departmentPageRequest.cursor());
            }
        }

        return queryFactory
            .selectFrom(department)
            .where(
                nameOrDescContains(departmentPageRequest.nameOrDescription()),
                cursorCondition(sortField, isAsc, departmentPageRequest.cursor(), lastDateValue, departmentPageRequest.idAfter())
            )
            .orderBy(
                getOrderSpecifier(sortField, isAsc),
                isAsc ? department.id.asc() : department.id.desc()
            )
            .limit(pageable.getPageSize()) // size + 1개만 가져와줘
            .fetch(); // 쿼리 실행문
    }

    @Override
    public long countByNameOrDescription(String nameOrDescription) {
        return Optional.ofNullable(queryFactory
            .select(department.count())
            .from(department)
            .where(nameOrDescContains(nameOrDescription)) // 기존 메서드 재사용
            .fetchOne()
        ).orElse(0L);
    }

    @Override
    public List<Department> findAllForExcel() {
        return queryFactory
            .selectFrom(department)
            .leftJoin(department.employees, employee).fetchJoin() // 소속된 직원이 없는 부서도 조회되어야하기 때문에 leftJoin 사용
            .fetch();
    }

    // 이름 또는 설명으로 검색
    private BooleanExpression nameOrDescContains(String nameOrDesc) {
        if (!StringUtils.hasText(nameOrDesc)) {
            return null;
        }
        return department.name.contains(nameOrDesc)
            .or(department.description.contains(nameOrDesc));
    }

    // 커서 기반 페이징 처리 로직
    private BooleanExpression cursorCondition(String sortField, boolean isAsc, String lastValue,
        LocalDate lastDateValue, Long lastId) {
        // 첫번째 페이지인 경우
        if (lastId == null) {
            return null;
        }

        // 1. 이름 정렬인 경우의 커서 로직
        if ("name".equals(sortField)) {
            return isAsc
                ? department.name.gt(lastValue) // lastValue보다 사전순 뒷쪽인 부서명(greater than)
                .or(department.name.eq(lastValue).and(department.id.gt(lastId))) // 부서명이 같을 경우 lastValue보다 Id가 큰 부서
                : department.name.lt(lastValue) // 내림차순일 경우 lastValue보다 사전순 앞쪽인 부서명(less than)
                    .or(department.name.eq(lastValue).and(department.id.lt(lastId))); // 부서명이 같을 경우 lastValue보다 Id가 작은 부서
        }

        // 2. 그 외 모든 경우(기본값인 설립일 포함)의 커서 로직
        return isAsc
            ? department.establishedDate.gt(lastDateValue) // lastDateValue보다 설립일이 뒤인 부서
            .or(department.establishedDate.eq(lastDateValue).and(department.id.gt(lastId))) // 설립일이 같을 경우 마지막으로 본 부서보다 id가 큰 부서
            : department.establishedDate.lt(lastDateValue) // 내림차순 일 경우 lastDateValue보다 설립일이 과거인 부서
                .or(department.establishedDate.eq(lastDateValue).and(department.id.lt(lastId))); // 설립일이 같을 경우 마지막으로 본 부서보다 id가 작은 부서
    }

    // 정렬 대상이 부서명 순(String)과 설립일 순(LocalDate)으로 타입이 다양하기 때문에 제네릭을 사용
    private OrderSpecifier<?> getOrderSpecifier(String sortField, boolean isAsc) {
        if ("name".equals(sortField)) {
            return isAsc ? department.name.asc() : department.name.desc();
        }

        // sortField가 null이거나 establishedDate는 설립일 정렬 반환
        return isAsc ? department.establishedDate.asc() : department.establishedDate.desc();
    }
}
