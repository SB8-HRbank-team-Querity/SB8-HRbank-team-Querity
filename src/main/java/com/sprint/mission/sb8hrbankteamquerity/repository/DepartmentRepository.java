package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);

    @Query("""
    SELECT d FROM Department d
    WHERE (:nameOrDesc IS NULL OR d.name LIKE %:nameOrDesc% OR d.description LIKE %:nameOrDesc%)
    AND (
        :idAfter IS NULL
        OR (:isAsc = true AND d.id > :idAfter)
        OR (:isAsc = false AND d.id < :idAfter)
    )
""")
    List<Department> findAllByCursor(
        @Param("nameOrDesc") String nameOrDescription,
        @Param("idAfter") Long idAfter,
        @Param("isAsc") boolean isAsc,
        Pageable pageable
    );

    @Query("""
        SELECT COUNT(d)
        FROM Department d
        WHERE (:nameOrDesc IS NULL OR :nameOrDesc = ''
               OR d.name LIKE %:nameOrDesc%
               OR d.description LIKE %:nameOrDesc%)
    """)
    long countByNameOrDescription(@Param("nameOrDesc") String nameOrDescription);
}
