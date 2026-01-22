package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryDTO;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EmployeeHistoryMapper {
    EmployeeHistoryDTO toDto(EmployeeHistory employeeHistory);
    EmployeeHistory toEntity(EmployeeHistoryDTO employeeHistoryDTO);
}
