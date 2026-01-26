package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DepartmentRepositoryCustom {

    List<Department> findAllByCursor(DepartmentPageRequest departmentPageRequest, Pageable pageable);

    long countByNameOrDescription(String nameOrDescription);
}
