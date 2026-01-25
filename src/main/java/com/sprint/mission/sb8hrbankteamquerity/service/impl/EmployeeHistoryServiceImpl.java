package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
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
    public List<ChangeLogDto> getAllEmployeeHistory(
            EmployeeHistoryFilter filter
        ) {
        Instant from = filter.atFrom() == null ? null :
            LocalDate.parse(filter.atFrom()).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant to = filter.atTo() == null ? null :
            LocalDate.parse(filter.atTo()).atStartOfDay().toInstant(ZoneOffset.UTC);

        String employeeNumber =
            (filter.employeeNumber() == null || filter.employeeNumber().isBlank()) ? null : filter.employeeNumber();

        String memo =
            (filter.memo() == null || filter.memo().isBlank()) ? null : filter.memo();

        String ipAddress =
            (filter.ipAddress() == null || filter.ipAddress().isBlank()) ? null : filter.ipAddress();

        String sortField = filter.sortField() == null ? "id" : filter.sortField();
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.direction().toString()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(0, 21, sort);

        // 조회
        Page<EmployeeHistory> employeeHistoryList = employeeHistoryRepository.findAllFilter(
            filter.idAfter(), employeeNumber, filter.type(),  memo, ipAddress, from, to, pageable);

        List<ChangeLogDto> changeLogDtoList = employeeHistoryList.stream().map(employeeHistoryMapper::toGetResponse).toList();

        return employeeHistoryList.map(employeeHistoryMapper::toGetResponse).getContent();
    }

    @Override
    public ChangeLogDetailDto getEmployeeHistoryById(Long employeeHistoryId) {
        EmployeeHistory employeeHistory =
            employeeHistoryRepository.findById(employeeHistoryId).
                orElseThrow(() -> new NullPointerException("찾을 수 없는 이력입니다."));

        return employeeHistoryMapper.toDetailResponse(employeeHistory);
    }
}
