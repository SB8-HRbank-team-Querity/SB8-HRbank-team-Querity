package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import com.sprint.mission.sb8hrbankteamquerity.service.criteriaAPI.EmployeeHistorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final EmployeeHistoryMapper employeeHistoryMapper;

    /*요청 예시
    EmployeeHistoryService.saveEmployeeHistory(
        new EmployeeHistorySaveRequest(
            EmployeeHistoryType.CREATED, //EmployeeHistoryType 중 하나 하시면 됩니다.
            "메모내용",
            "ip 주소 값",
            EmployeeHistoryMapper.toChangedDetail(EmployeeDto newDto, EmployeeDto oldDto),
            "직원이름",
            "사원번호"
        ));
        */
    @Override
    public ChangeLogDto saveEmployeeHistory(EmployeeHistorySaveRequest save) {
        EmployeeHistory entity = employeeHistoryMapper.toEntity(save);

        return employeeHistoryMapper.toGetResponse(
            employeeHistoryRepository.save(entity)
        );
    }


    @Override
    public List<ChangeLogDto> getAllEmployeeHistory(EmployeeHistoryFilter filter) {

        int size = filter.size() != null && filter.size() > 0
            ? filter.size() : 10;

        // 기본 정렬 기준, iP
        String sortField = filter.sortField() == null ? "ip" : filter.sortField();
        // 기본 출력 순서, 내림차순
        String direc = filter.direction() == null ? "desc" : filter.direction().toString();

        Sort.Direction direction = "desc".equalsIgnoreCase(direc) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(0, size+1, sort);

        Slice<EmployeeHistory> slice =
            employeeHistoryRepository.findAll(
                EmployeeHistorySpecification.filter(filter),
                pageable
            );

        return slice
            .map(employeeHistoryMapper::toGetResponse)
            .getContent();
    }


    @Override
    public ChangeLogDetailDto getEmployeeHistoryById(Long employeeHistoryId) {
        EmployeeHistory employeeHistory =
            employeeHistoryRepository.findById(employeeHistoryId).
                orElseThrow(() -> new NullPointerException("찾을 수 없는 이력입니다."));

        return employeeHistoryMapper.toDetailResponse(employeeHistory);
    }

    @Override
    public Long getEmployeeHistoryCount(String fromDate, String toDate) {

        Instant end = null;
        Instant start = null;

        // 파라미터가 없으면 기본적으로 최근 1주일 출력
        if (fromDate == null && toDate == null) {
            end = Instant.now();
            start = end.minus(7, ChronoUnit.DAYS);

        } else if ((fromDate == null && toDate != null)
            || (fromDate != null && toDate == null)) {
            // 날짜를 하나만 입력 했을 경우 1, 그날 하루치만 나오게

            String day = fromDate == null ? toDate : fromDate;

            start = parsingStratDate(day);
            end = parsingEndDate(day);

        } else {
            // 2개를 입력 했을 경우
            start = parsingStratDate(fromDate);
            end = parsingEndDate(toDate);
        }

        return employeeHistoryRepository.countEmployeeHistoryByCreatedAtBetween(start, end);
    }

    private Instant parsingEndDate(String date) {
        return LocalDate.parse(date)
            .plusDays(1)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC);
    }

    private Instant parsingStratDate(String date) {
        return LocalDate.parse(date)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC);
    }
}