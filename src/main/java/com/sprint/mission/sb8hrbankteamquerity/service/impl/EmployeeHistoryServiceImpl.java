package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryGetResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final EmployeeHistoryMapper employeeHistoryMapper;

    @Override
    public EmployeeHistoryGetResponse saveEmployeeHistory(EmployeeHistorySaveRequest employeeHistorySaveRequest) {
        // 필요한 로직이 저장이 됐는지 확인만 있으면 될거 같은데
        EmployeeHistory employeeHistory =
            employeeHistoryRepository.save(
                employeeHistoryMapper.toEntity(employeeHistorySaveRequest));

        return employeeHistoryMapper.toGetResponse(employeeHistory);
    }

    @Override
    public List<EmployeeHistoryGetResponse> getAllEmployeeHistory() {
        List<EmployeeHistoryGetResponse> employeeHistoryGetResponseList =
            employeeHistoryRepository.findAll().stream().
                map(employeeHistoryMapper::toGetResponse).toList();

        return employeeHistoryGetResponseList;
    }


    @Override
    public EmployeeHistoryGetResponse getByIdEmployeeHistory(Long employeeHistoryId) {
        EmployeeHistory employeeHistory =
            employeeHistoryRepository.findById(employeeHistoryId).
            orElseThrow(()-> new NullPointerException("찾을 수 없는 이력입니다."));

        return employeeHistoryMapper.toGetResponse(employeeHistory);
    }
}
