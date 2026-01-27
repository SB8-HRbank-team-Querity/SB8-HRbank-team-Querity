package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistorySearchCondition;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import static com.sprint.mission.sb8hrbankteamquerity.entity.QBackupHistory.backupHistory;
import static com.sprint.mission.sb8hrbankteamquerity.entity.QFileMeta.fileMeta;

@Repository
@RequiredArgsConstructor
public class BackupHistoryRepositoryImpl implements BackupHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BackupHistory> findAllByCursor(
        BackupHistorySearchCondition condition,
        Pageable pageable
    ) {
        return queryFactory
            .selectFrom(backupHistory)
            // N+1 문제 해결!!
            .leftJoin(backupHistory.fileMeta, fileMeta).fetchJoin()
            .where(
                // 작업자 내용을 포함하는가?
                containsWorker(condition.worker()),
                // 상태 내용과 일치하는가?
                eqStatus(condition.statusFilter()),
                // 시작시간(부터) - 시작시간(까지) 사이에 있는가?
                betweenStartedAt(condition.startedAtFrom(), condition.startedAtTo()),
                // 커서 조건
                cursorCondition(condition.cursorId(), condition.cursorValue(), condition.sortField(), condition.direction())
            )
            .orderBy(
                // 정렬 조건
                getOrderSpecifier(condition.sortField(), condition.direction()),
                // 시간이 같을 경우 아이디 값을 비교해서 정렬
                condition.direction().isAscending() ? backupHistory.id.asc() : backupHistory.id.desc()
            )
            // 페이징 처리
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public Long countByCondition(BackupHistorySearchCondition condition) {
        Long count = queryFactory
            .select(backupHistory.count())
            .from(backupHistory)
            .where(
                containsWorker(condition.worker()),
                eqStatus(condition.statusFilter()),
                betweenStartedAt(condition.startedAtFrom(), condition.startedAtTo())
            )
            .fetchOne();
        return count != null ? count : 0L;
    }

    // Status를 숫자로 변환하는 메소드 (순서 정의) - 커서 페이지네이션의 무결성을 지키기 위한 넘버링
    private NumberExpression<Integer> getStatusPriority() {
        return new CaseBuilder()
            .when(backupHistory.status.eq(BackupHistoryStatus.COMPLETED)).then(1)
            .when(backupHistory.status.eq(BackupHistoryStatus.FAILED)).then(2)
            .when(backupHistory.status.eq(BackupHistoryStatus.IN_PROGRESS)).then(3)
            .when(backupHistory.status.eq(BackupHistoryStatus.SKIPPED)).then(4)
            //정의되지 않은 상태, 가장 뒤로 뺌
            .otherwise(99);
    }

    // 작업 상태값을 가져와서 숫자로 변환
    private int getPriorityByStatus(BackupHistoryStatus status) {
        switch (status) {
            case COMPLETED:
                return 1;
            case FAILED:
                return 2;
            case IN_PROGRESS:
                return 3;
            case SKIPPED:
                return 4;
            default:
                return 99;
        }
    }

    // 검색 조건에 관리자가 포함되어 있는가?
    private BooleanExpression containsWorker(String worker) {
        return (worker != null && !worker.isBlank()) ? backupHistory.worker.contains(worker) : null;
    }

    // 검색 조건에 작업 상태가 포함되어 있는가?
    private BooleanExpression eqStatus(BackupHistoryStatus status) {
        return status != null ? backupHistory.status.eq(status) : null;
    }

    // 검색 조건에 시작 시간(From), 시작 시간(To)가 포함되어 있는가?
    private BooleanExpression betweenStartedAt(Instant from, Instant to) {
        if (from == null && to == null) {
            return null;
        }
        // Less Or Equal
        if (from == null) return backupHistory.startedAt.loe(to);
        // Greater Or Equal
        if (to == null) return backupHistory.startedAt.goe(from);

        return backupHistory.startedAt.between(from, to);
    }

    // 커서 페이징 조건
    private BooleanExpression cursorCondition(Long cursorId, String cursorValue, String sortField, Sort.Direction direction) {
        if (cursorId == null || cursorValue == null) {
            return null;
        }

        boolean isAsc = direction.isAscending();

        if ("status".equals(sortField)) {
            BackupHistoryStatus cursorStatus = BackupHistoryStatus.valueOf(cursorValue);
            int cursorPriority = getPriorityByStatus(cursorStatus);

            if (isAsc) {
                // DB에서 변환된 숫자(getStatusPriority)와 숫자(cursorPriority)를 비교
                // ASC : Priority > cursorPriority OR (Priority == cursorPriority AND id > cursorId)
                return getStatusPriority().gt(cursorPriority)
                    .or(getStatusPriority().eq(cursorPriority).and(backupHistory.id.gt(cursorId)));
            } else {
                // DESC: status < cursor OR (status == cursor AND id < cursorId)
                return getStatusPriority().lt(cursorPriority)
                    .or(getStatusPriority().eq(cursorPriority).and(backupHistory.id.lt(cursorId)));
            }
        }

        // 종료 시간 기준 정렬
        else if ("endedAt".equals(sortField)) {
            Instant cursorTime = Instant.parse(cursorValue);
            if (isAsc) {
                return backupHistory.endedAt.gt(cursorTime)
                    .or(backupHistory.endedAt.eq(cursorTime).and(backupHistory.id.gt(cursorId)));
            } else {
                return backupHistory.endedAt.lt(cursorTime)
                    .or(backupHistory.endedAt.eq(cursorTime).and(backupHistory.id.lt(cursorId)));
            }
        }

        // 시작시간 기준 (기본 값)
        else {
            Instant cursorTime = Instant.parse(cursorValue);
            if (isAsc) {
                return backupHistory.startedAt.gt(cursorTime)
                    .or(backupHistory.startedAt.eq(cursorTime).and(backupHistory.id.gt(cursorId)));
            } else {
                return backupHistory.startedAt.lt(cursorTime)
                    .or(backupHistory.startedAt.eq(cursorTime).and(backupHistory.id.lt(cursorId)));
            }
        }
    }

    // 요청된 정렬 필드(sortField)에 따라 QueryDSL 정렬 기준(OrderSpecifier)을 반환
    // 동적 정렬 조건 처리
    private OrderSpecifier<?> getOrderSpecifier(String sortField, Sort.Direction direction) {
        Order order = direction.isAscending() ? Order.ASC : Order.DESC;

        if ("endedAt".equals(sortField)) {
            return new OrderSpecifier<>(order, backupHistory.endedAt);
        } else if ("status".equals(sortField)) {
            return new OrderSpecifier<>(order, getStatusPriority());
        }
        // 기본값: startedAt
        return new OrderSpecifier<>(order, backupHistory.startedAt);
    }
}
