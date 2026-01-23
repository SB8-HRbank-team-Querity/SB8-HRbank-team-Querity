package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);

    @Query("select distinct d from Department d left join fetch d.employees")
    List<Department> findAllWithEmployees();
}
