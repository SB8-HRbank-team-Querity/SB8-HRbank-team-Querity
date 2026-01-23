package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import com.sprint.mission.sb8hrbankteamquerity.service.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
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
            mapper.toChangedDetail(EmployeeDto),
            "직원이름",
            "사원번호"
        ));
        */
    @Override
    public EmployeeHistoryResponse saveEmployeeHistory(EmployeeHistorySaveRequest save) {
        EmployeeHistory entity = EmployeeHistory.builder()
            .type(save.type())
            .memo(save.memo())
            .ip_address(save.ip())
            .changed_detail(save.changed_detail())
            .employee_name(save.employee_name())
            .employee_number(save.employee_number())
            .build();

        return employeeHistoryMapper.toGetResponse(
            employeeHistoryRepository.save(entity)
        );
    }

    @Override
    public List<EmployeeHistoryResponse> getAllEmployeeHistory() {
        List<EmployeeHistoryResponse> employeeHistoryResponseList =
            employeeHistoryRepository.findAll().stream().
                map(employeeHistoryMapper::toGetResponse).toList();

        return employeeHistoryResponseList;
    }

    @Override
    public EmployeeHistoryResponse getByIdEmployeeHistory(Long employeeHistoryId) {
        EmployeeHistory employeeHistory =
            employeeHistoryRepository.findById(employeeHistoryId).
                orElseThrow(() -> new NullPointerException("찾을 수 없는 이력입니다."));

        return employeeHistoryMapper.toGetResponse(employeeHistory);
    }
}
