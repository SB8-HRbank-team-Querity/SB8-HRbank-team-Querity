package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryDTO;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final EmployeeHistoryMapper employeeHistoryMapper;

    @Override
    public ResponseEntity<EmployeeHistoryDTO> saveEmployeeHistory(EmployeeHistorySaveRequest employeeHistorySaveRequest) {
        // 필요한 로직이 저장이 됐는지 확인만 있으면 될거 같은데
        EmployeeHistory employeeHistory =
            employeeHistoryRepository.save(new EmployeeHistory(employeeHistorySaveRequest));

        if (employeeHistory != null) {
            EmployeeHistoryDTO employeeHistoryDTO = employeeHistoryMapper.toDto(employeeHistory);
            return ResponseEntity.ok(employeeHistoryDTO);
        }

        return ResponseEntity.notFound().build();
    }
}
