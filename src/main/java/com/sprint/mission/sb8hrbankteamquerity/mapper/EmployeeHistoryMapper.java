package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.DiffDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
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

    ChangeLogDto toGetResponse(EmployeeHistory employeeHistory);

    EmployeeHistory toEntity(EmployeeHistorySaveRequest save);

//    여기 다시 해야함
    default List<DiffDto> toChangedDetail(EmployeeDto newDto, EmployeeDto oldDto) {
        List<DiffDto> diffDtoList = new ArrayList<>();

        compareAndAdd(diffDtoList,"hireDate",oldDto.hireDate(),newDto.hireDate());
        compareAndAdd(diffDtoList,"name",oldDto.name(),newDto.name());
        compareAndAdd(diffDtoList,"position",oldDto.position(),newDto.position());
        compareAndAdd(diffDtoList,"departmentName",oldDto.departmentName(),newDto.departmentName());
        compareAndAdd(diffDtoList,"email",oldDto.email(),newDto.email());
        compareAndAdd(diffDtoList,"employeeNumber",oldDto.employeeNumber(),newDto.employeeNumber());
        compareAndAdd(diffDtoList,"status",oldDto.status(),newDto.status());

        return diffDtoList;
    }

    private void compareAndAdd(List<DiffDto> diffDtoList, String key, Object oldValue, Object newValue) {

        if (oldValue == null && newValue == null) return;
        if (oldValue != null && oldValue.equals(newValue)) return;

        if (!newValue.equals(oldValue)) {
            DiffDto diffDto = new DiffDto(key,oldValue.toString(),newValue.toString());
            diffDtoList.add(diffDto);
        }
    }
}
