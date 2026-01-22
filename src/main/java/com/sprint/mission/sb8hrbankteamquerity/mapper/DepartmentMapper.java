package com.sprint.mission.sb8hrbankteamquerity.mapper;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    public DepartmentDto toDto(Department department) {
        if (department == null) {
            return null;
        }

        int employeeCount = (department.getEmployees() != null) ? department.getEmployees().size() : 0;

        return new DepartmentDto(
            department.getId(),
            department.getName(),
            department.getDescription(),
            department.getEstablishedDate(),
            employeeCount
        );
    }
}
