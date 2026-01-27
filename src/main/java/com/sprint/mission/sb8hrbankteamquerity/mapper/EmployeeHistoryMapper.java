package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.DiffDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

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

    default List<DiffDto> toChangedDetail(EmployeeDto newDto, EmployeeDto oldDto) {
        List<DiffDto> diffDto = new ArrayList<>();

        if (oldDto == null) {
            compareAndAdd(diffDto, "hireDate", null, newDto.hireDate());
            compareAndAdd(diffDto, "name", null, newDto.name());
            compareAndAdd(diffDto, "position", null, newDto.position());
            compareAndAdd(diffDto, "department", null, newDto.departmentName());
            compareAndAdd(diffDto, "email", null, newDto.email());
            compareAndAdd(diffDto, "employeeNumber", null, newDto.employeeNumber());
            compareAndAdd(diffDto, "status", null, newDto.status());
            return diffDto;
        }
        else if(newDto == null) {
            compareAndAdd(diffDto, "hireDate", oldDto.hireDate(),null);
            compareAndAdd(diffDto, "name",  oldDto.name(),null);
            compareAndAdd(diffDto, "position", oldDto.position(),null);
            compareAndAdd(diffDto, "department",  oldDto.departmentName(),null);
            compareAndAdd(diffDto, "email", oldDto.email(),null);
            compareAndAdd(diffDto, "employeeNumber", oldDto.employeeNumber(),null);
            compareAndAdd(diffDto, "status", oldDto.status(), null);
            return diffDto;
        }

        compareAndAdd(diffDto, "hireDate", oldDto.hireDate(), newDto.hireDate());
        compareAndAdd(diffDto, "name", oldDto.name(), newDto.name());
        compareAndAdd(diffDto, "position", oldDto.position(), newDto.position());
        compareAndAdd(diffDto, "department", oldDto.departmentName(), newDto.departmentName());
        compareAndAdd(diffDto, "email", oldDto.email(), newDto.email());
        compareAndAdd(diffDto, "employeeNumber", oldDto.employeeNumber(), newDto.employeeNumber());
        compareAndAdd(diffDto, "status", oldDto.status(), newDto.status());

        return diffDto;
    }

    private void compareAndAdd(List<DiffDto> diffDto, String key, Object oldValue, Object newValue) {

        if (oldValue != null && oldValue.equals(newValue)) return;

        String oldStr = (oldValue != null) ? String.valueOf(oldValue) : null;
        String newStr = (newValue != null) ? String.valueOf(newValue) : null;

        diffDto.add(new DiffDto(key, oldStr, newStr));
    }
}
