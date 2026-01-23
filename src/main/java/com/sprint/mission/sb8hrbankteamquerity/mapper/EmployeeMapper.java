package com.sprint.mission.sb8hrbankteamquerity.mapper;


import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EmployeeMapper {
    @Mapping(source = "departmentId.id", target = "departmentId")
    @Mapping(source = "departmentId.name", target = "departmentName")
    @Mapping(source = "profileImageId", target = "profileImageId")
    EmployeeDto toDto(Employee employee);
}

