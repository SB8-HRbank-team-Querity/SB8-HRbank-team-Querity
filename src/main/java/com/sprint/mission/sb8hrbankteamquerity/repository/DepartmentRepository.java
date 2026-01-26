package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);

    @Query("SELECT d FROM Department d " +
        "WHERE (:nameOrDesc IS NULL OR d.name LIKE CONCAT('%', :nameOrDesc, '%') OR d.description LIKE CONCAT('%', :nameOrDesc, '%')) "
        +
        "AND ( " +
        "    :lastId IS NULL OR " +
        "    ( " +
        "        (:sortField = 'name' AND ( " +
        "            (:isAsc = true AND (d.name > :lastValue OR (d.name = :lastValue AND d.id > :lastId))) OR "
        +
        "            (:isAsc = false AND (d.name < :lastValue OR (d.name = :lastValue AND d.id < :lastId))) "
        +
        "        )) " +
        "        OR " +
        "        (:sortField = 'establishedDate' AND ( " +
        "            (:isAsc = true AND (d.establishedDate > :lastDateValue OR (d.establishedDate = :lastDateValue AND d.id > :lastId))) OR "
        +
        "            (:isAsc = false AND (d.establishedDate < :lastDateValue OR (d.establishedDate = :lastDateValue AND d.id < :lastId))) "
        +
        "        )) " +
        "    ) " +
        ") ")
    List<Department> findAllByCursor(
        @Param("nameOrDesc") String nameOrDescription,
        @Param("lastValue") String lastValue, // 마지막 데이터의 이름 또는 날짜
        @Param("lastDateValue") LocalDate lastDateValue,
        @Param("lastId") Long lastId,         // 마지막 데이터의 ID
        @Param("sortField") String sortField,
        @Param("isAsc") boolean isAsc,
        Pageable pageable
    );

    @Query("""
            SELECT COUNT(d)
            FROM Department d
            WHERE (:nameOrDesc IS NULL OR :nameOrDesc = ''
                   OR d.name LIKE CONCAT('%', :nameOrDesc, '%')
                   OR d.description LIKE CONCAT('%', :nameOrDesc, '%'))
        """)
    long countByNameOrDescription(@Param("nameOrDesc") String nameOrDescription);
}
