package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e " +
        "WHERE (:idAfter IS NULL OR e.id > :idAfter) " +
        "AND (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%) " +
        "AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%) " +
        "AND (:departmentName IS NULL OR e.departmentId.name LIKE %:departmentName%) " +
        "AND (:position IS NULL OR e.position LIKE %:position%) " +
        "AND (:hireDateFrom IS NULL OR e.hireDate >= CAST(:hireDateFrom AS date))" +
        "AND (:hireDateTo   IS NULL OR e.hireDate <= CAST(:hireDateTo AS date))" +
        "AND (:status IS NULL OR e.status = :status)")
    List<Employee> findAllFilter(@Param("idAfter") Long idAfter,
                                 @Param("nameOrEmail") String nameOrEmail,
                                 @Param("employeeNumber") String employeeNumber,
                                 @Param("departmentName") String departmentName,
                                 @Param("position") String position,
                                 @Param("hireDateFrom") LocalDate hireDateFrom,
                                 @Param("hireDateTo") LocalDate hireDateTo,
                                 @Param("status") EmployeeStatus status,
                                 Pageable pageable
    );

    @Query(value = "SELECT e FROM Employee e JOIN FETCH e.departmentId",
        countQuery = "SELECT count(e) FROM Employee e"
    )
    Page<Employee> findAllWithDepartment(Pageable pageable);

    Boolean existsByEmail(String email);
}

