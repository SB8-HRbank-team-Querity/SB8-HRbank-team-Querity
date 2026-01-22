package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAll();
}
