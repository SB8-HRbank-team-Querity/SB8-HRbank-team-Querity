package com.sprint.mission.sb8hrbankteamquerity.mapper;


import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EmployeeMapper {
    EmployeeDto toDto(Employee employee);
}
