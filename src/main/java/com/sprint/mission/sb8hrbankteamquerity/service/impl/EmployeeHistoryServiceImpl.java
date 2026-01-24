package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<ChangeLogDto> getAllEmployeeHistory() {
        List<ChangeLogDto> changeLogDtoList =
            employeeHistoryRepository.findAll().stream().
                map(employeeHistoryMapper::toGetResponse).toList();

        return changeLogDtoList;
    }

    @Override
    public ChangeLogDetailDto getEmployeeHistoryById(Long employeeHistoryId) {
        EmployeeHistory employeeHistory =
            employeeHistoryRepository.findById(employeeHistoryId).
                orElseThrow(() -> new NullPointerException("찾을 수 없는 이력입니다."));

        return employeeHistoryMapper.toDetailResponse(employeeHistory);
    }
}
