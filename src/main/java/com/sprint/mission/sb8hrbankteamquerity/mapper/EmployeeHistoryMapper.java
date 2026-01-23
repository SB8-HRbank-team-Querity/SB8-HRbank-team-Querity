package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashMap;
import java.util.Map;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EmployeeHistoryMapper {

    EmployeeHistoryResponse toGetResponse(EmployeeHistory employeeHistory);

    EmployeeHistory toEntity(EmployeeHistorySaveRequest save);

    default Map<String, Object> toChangedDetail(EmployeeDto newDto, EmployeeDto oldDto) {
        Map<String, Object> map = new HashMap<>();

        compareAndAdd(map,"hireDate",oldDto.hireDate(),newDto.hireDate());
        compareAndAdd(map,"name",oldDto.name(),newDto.name());
        compareAndAdd(map,"position",oldDto.position(),newDto.position());
        compareAndAdd(map,"departmentName",oldDto.departmentName(),newDto.departmentName());
        compareAndAdd(map,"email",oldDto.email(),newDto.email());
        compareAndAdd(map,"employeeNumber",oldDto.employeeNumber(),newDto.employeeNumber());
        compareAndAdd(map,"status",oldDto.status(),newDto.status());

        return map;
    }

    private void compareAndAdd(Map<String, Object> map, String key, Object oldValue, Object newValue) {
        if (newValue != null && !newValue.equals(oldValue)) {
            map.put(key, oldValue);
        }
    }
}
