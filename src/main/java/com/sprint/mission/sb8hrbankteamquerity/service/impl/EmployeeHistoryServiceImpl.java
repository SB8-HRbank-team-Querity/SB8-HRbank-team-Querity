package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
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
    private final HttpServletRequest httpServletRequest;

    @Override
    public EmployeeHistoryResponse saveEmployeeHistory(EmployeeHistorySaveRequest save) {

        String clientIp = getClientIp();

        EmployeeHistory entity = EmployeeHistory.builder()
            .type(save.type())
            .memo(save.memo())
            .ip_address(clientIp)
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


    private String getClientIp() {
        String[] headerCandidates = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA"
        };

        for (String header : headerCandidates) {
            String ip = httpServletRequest.getHeader(header);
            if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return httpServletRequest.getRemoteAddr();
    }
}
