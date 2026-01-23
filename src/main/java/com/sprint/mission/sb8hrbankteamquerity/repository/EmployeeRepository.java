package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e " +
        "WHERE (:idAfter IS NULL OR e.id > :idAfter) " +
        "AND (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%) " +
        "AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%) " +
        "AND (:departmentName IS NULL OR e.departmentId.name LIKE %:departmentName%) " +
        "AND (:position IS NULL OR e.position LIKE %:position%) " +
        "AND (e.hireDate >= COALESCE(:hireDateFrom, e.hireDate)) " +
        "AND (e.hireDate <= COALESCE(:hireDateTo, e.hireDate)) " +
        "AND (:status IS NULL OR e.status = :status)")
    List<Employee> findAllFilter(@Param("idAfter") Long idAfter,
                                 @Param("nameOrEmail") String nameOrEmail,
                                 @Param("employeeNumber") String employeeNumber,
                                 @Param("departmentName") String departmentName,
                                 @Param("position") String position,
                                 @Param("hireDateFrom") Instant hireDateFrom,
                                 @Param("hireDateTo") Instant hireDateTo,
                                 @Param("status") EmployeeStatus status,
                                 Pageable pageable
    );
}
