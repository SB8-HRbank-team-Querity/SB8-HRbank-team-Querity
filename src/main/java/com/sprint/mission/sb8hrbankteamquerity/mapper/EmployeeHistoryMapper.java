package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.DiffDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EmployeeHistoryMapper {
    @Mapping(source = "createdAt", target = "at")
    @BeanMapping(ignoreUnmappedSourceProperties = {
        "changed_detail"
    })
    ChangeLogDto toGetResponse(EmployeeHistory employeeHistory);


    @Mapping(source = "createdAt", target = "at")
    @Mapping(source = "changed_detail", target = "diffs")
    ChangeLogDetailDto toDetailResponse(EmployeeHistory employeeHistory);

    EmployeeHistory toEntity(EmployeeHistorySaveRequest save);

    default Map<String, DiffDto> toChangedDetail(EmployeeDto newDto, EmployeeDto oldDto) {
        Map<String, DiffDto> diffDto = new HashMap<>();

        compareAndAdd(diffDto, "hireDate", oldDto.hireDate(), newDto.hireDate());
        compareAndAdd(diffDto, "name", oldDto.name(), newDto.name());
        compareAndAdd(diffDto, "position", oldDto.position(), newDto.position());
        compareAndAdd(diffDto, "departmentName", oldDto.departmentName(), newDto.departmentName());
        compareAndAdd(diffDto, "email", oldDto.email(), newDto.email());
        compareAndAdd(diffDto, "employeeNumber", oldDto.employeeNumber(), newDto.employeeNumber());
        compareAndAdd(diffDto, "status", oldDto.status(), newDto.status());

        return diffDto;
    }

    private void compareAndAdd(Map<String, DiffDto> diffDto, String key, Object oldValue, Object newValue) {

        if (newValue == null) return;
        if (oldValue != null && oldValue.equals(newValue)) return;

        String oldStr = (oldValue != null) ? String.valueOf(oldValue) : null;
        String newStr = (newValue != null) ? String.valueOf(newValue) : null;

        diffDto.put(key, new DiffDto(key, oldStr, newStr));
    }
}
