package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EmployeeHistoryMapper {
    EmployeeHistoryResponse toGetResponse(EmployeeHistory employeeHistory);

    @Mapping(target = "type", source = "type")
    @Mapping(target = "memo", source = "memo")
    @Mapping(target = "ip", source = "ip")
    @Mapping(
        target = "changed_detail",
        expression = "java(toChangedDetail(employeeDto))"
    )
    @Mapping(target = "employee_name", source = "employeeDto.name")
    @Mapping(target = "employee_number", source = "employeeDto.employeeNumber")
    EmployeeHistorySaveRequest toSaveRequest(
        EmployeeDto employeeDto,
        EmployeeHistoryType type,
        String memo,
        String ip
    );

    default Map<String, Object> toChangedDetail(EmployeeDto dto) {
        Map<String, Object> map = new HashMap<>();

        map.put("hireDate", dto.hireDate());
        map.put("name", dto.name());
        map.put("position", dto.position());
        map.put("departmentName", dto.departmentName());
        map.put("email", dto.email());
        map.put("employeeNumber", dto.employeeNumber());
        map.put("status", dto.status());

        return map;
    }
}
